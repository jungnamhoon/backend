package com.hkorea.skyisthelimit.service;

import com.hkorea.skyisthelimit.common.exception.BusinessException;
import com.hkorea.skyisthelimit.common.response.ErrorCode;
import com.hkorea.skyisthelimit.common.utils.ImageUtils;
import com.hkorea.skyisthelimit.common.utils.QueryDSLHelper;
import com.hkorea.skyisthelimit.common.utils.mapper.StudyMapper;
import com.hkorea.skyisthelimit.common.utils.mapper.StudyProblemMapper;
import com.hkorea.skyisthelimit.dto.criteria.PageableCriteria;
import com.hkorea.skyisthelimit.dto.problem.request.DailyProblemsCreateRequest;
import com.hkorea.skyisthelimit.dto.problem.response.DailyProblemCreateResponse;
import com.hkorea.skyisthelimit.dto.study.request.StudyCreateRequest;
import com.hkorea.skyisthelimit.dto.study.request.StudyUpdateRequest;
import com.hkorea.skyisthelimit.dto.study.response.StudyCreateResponse;
import com.hkorea.skyisthelimit.dto.study.response.StudyInfoResponse;
import com.hkorea.skyisthelimit.dto.study.response.StudySummaryResponse;
import com.hkorea.skyisthelimit.dto.study.response.StudyUpdateResponse;
import com.hkorea.skyisthelimit.dto.study.response.ThumbnailUpdateResponse;
import com.hkorea.skyisthelimit.entity.Member;
import com.hkorea.skyisthelimit.entity.MemberStudy;
import com.hkorea.skyisthelimit.entity.Problem;
import com.hkorea.skyisthelimit.entity.QStudy;
import com.hkorea.skyisthelimit.entity.Study;
import com.hkorea.skyisthelimit.entity.StudyProblem;
import com.hkorea.skyisthelimit.entity.embeddable.DailyProblem;
import com.hkorea.skyisthelimit.repository.StudyRepository;
import com.hkorea.skyisthelimit.service.enums.ImageType;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
public class StudyService {

  private final StudyRepository studyRepository;
  private final MemberService memberService;
  private final QueryDSLHelper queryDSLService;
  private final ProblemService problemService;
  private final MinioService minioService;

  @Value("${minio.endpoint}")
  private String minioEndpoint;

  @Transactional
  public Page<StudySummaryResponse> getStudyPage(PageableCriteria<QStudy> criteria) {

    QStudy study = QStudy.study;

    BooleanExpression predicate = criteria.toPredicate();
    OrderSpecifier<?> orderSpecifier = criteria.toOrderSpecifier(study);
    Pageable pageable = criteria.toPageable();

    List<Study> studies = queryDSLService.fetchEntities(study, predicate, orderSpecifier,
        pageable);

    long total = queryDSLService.fetchTotalCount(study, predicate);

    List<StudySummaryResponse> studySummaryResponseList = StudyMapper.toStudySummaryResponseList(
        studies);

    return new PageImpl<>(studySummaryResponseList, pageable, total);
  }

  @Transactional
  public StudyInfoResponse getStudyInfo(Integer studyId) {

    Study study = getStudy(studyId);

    if (study.isDailyProblemsNotUpToDate()) {
      study.clearDailyProblems();
    }

    List<StudyProblem> studyProblemListSolvedByAll = getStudyProblemListSolvedByAll(studyId);
    studyProblemListSolvedByAll.forEach(StudyProblem::markToSolved);

    int newSolvedCount = studyProblemListSolvedByAll.size();
    study.incrementTotalSolvedProblemsCount(newSolvedCount);

    Set<MemberStudy> memberStudiesNotSolvingDailyProblems = getMemberStudiesNotSolvingDailyProblems(
        studyId);

    if (allSolvedDailyProblems(memberStudiesNotSolvingDailyProblems)) {
      study.updateStreak();
    }

    return StudyMapper.toStudyInfoResponse(
        study,
        memberStudiesNotSolvingDailyProblems);
  }

  @Transactional
  public StudyUpdateResponse updateStudy(Integer studyId, String username,
      StudyUpdateRequest requestDTO) {

    Study study = getStudy(studyId);

    validateAdmin(username, study);

    study.update(requestDTO);

    return StudyMapper.toStudyUpdateResponse(study);
  }

  @Transactional
  public ThumbnailUpdateResponse updateThumbnail(Integer studyId, String username,
      MultipartFile studyProfileImage)

      throws ErrorResponseException, InsufficientDataException, InternalException,
      InvalidKeyException, InvalidResponseException, IOException, NoSuchAlgorithmException,
      ServerException, XmlParserException {

    Study study = getStudy(studyId);

    validateAdmin(username, study);

    if (studyProfileImage == null || studyProfileImage.isEmpty()) {
      return StudyMapper.toThumbnailUpdateResponse(study);
    }

    // 1. 기존 이미지 삭제
    minioService.deleteOldImage(study.getThumbnailUrl());

    ImageUtils.validateImage(studyProfileImage);

    byte[] thumbnail = ImageUtils.createThumbnail(studyProfileImage);

    String imageUrl = minioService.uploadImage(
        ImageType.STUDY,
        Integer.toString(study.getId()),
        thumbnail,
        studyProfileImage.getOriginalFilename(),
        studyProfileImage.getContentType());

    study.setThumbnailUrl(imageUrl);

    return StudyMapper.toThumbnailUpdateResponse(study);
  }

  @Transactional
  public StudyCreateResponse createStudy(String username, StudyCreateRequest requestDTO) {

    Member host = memberService.getMember(username);

    Study study = requestDTO.toEntity(host);
    study.setThumbnailUrl(minioEndpoint + "/skyisthelimit/study/basic-thumbnail.png");

    studyRepository.save(study);

    return StudyMapper.toStudyCreateResponse(study);
  }

  @Transactional
  public Set<DailyProblemCreateResponse> createDailyProblems(Integer studyId, String username,
      DailyProblemsCreateRequest requestDTO) {

    Study study = getStudy(studyId);

    validateQuestionSetter(username, study);
    validateDailyProblemsUpToDate(study);
    validateProblemCountMatch(study, requestDTO);

    study.clearDailyProblems();

    return registerTodayProblems(study, requestDTO);
  }

  public Study getStudy(Integer studyId) {
    return studyRepository.findById(studyId)
        .orElseThrow(() -> new BusinessException(ErrorCode.STUDY_NOT_FOUND));
  }

  private Set<DailyProblemCreateResponse> registerTodayProblems(Study study,
      DailyProblemsCreateRequest requestDTO) {

    return requestDTO.getProblemIds()
        .stream()
        .map(problemId -> {

          Problem problem = problemService.getOrRegisterProblem(problemId);

          StudyProblem studyProblem = StudyProblem.create(study, problem);
          study.addStudyProblem(studyProblem);

          DailyProblem dailyProblem = DailyProblem.create(problem);
          study.addDailyProblem(DailyProblem.create(problem));

          return StudyProblemMapper.toDailyProblemCreateResponse(dailyProblem);
        })
        .collect(Collectors.toSet());
  }

  private void validateAdmin(String username, Study study) {
    if (study.isNotAdmin(username)) {
      throw new BusinessException(ErrorCode.STUDY_UPDATE_FORBIDDEN);
    }
  }

  private void validateQuestionSetter(String username, Study study) {
    if (study.isNotQuestionSetter(username)) {
      throw new BusinessException(ErrorCode.NOT_QUESTION_SETTER);
    }
  }

  private void validateDailyProblemsUpToDate(Study study) {
    if (study.isDailyProblemsUpToDate()) {
      throw new BusinessException(ErrorCode.DUPLICATE_TODAY_PROBLEM);
    }
  }

  private void validateProblemCountMatch(Study study, DailyProblemsCreateRequest requestDTO) {
    int expectedCount = study.getDailyProblemCount();
    int actualCount = requestDTO.getProblemIds() != null ? requestDTO.getProblemIds().size() : 0;

    if (expectedCount != actualCount) {
      throw new BusinessException(ErrorCode.STUDY_PROBLEM_COUNT_MISMATCH);
    }
  }

  private List<StudyProblem> getStudyProblemListSolvedByAll(Integer studyId) {
    return studyRepository.findStudyProblemListSolvedByAll(
        studyId);
  }

  private Set<MemberStudy> getMemberStudiesNotSolvingDailyProblems(Integer studyId) {

    return studyRepository.findMemberStudiesNotSolvingDailyProblems(
        studyId);
  }

  private boolean allSolvedDailyProblems(Set<MemberStudy> memberStudiesNotSolvingDailyProblems) {
    return memberStudiesNotSolvingDailyProblems.isEmpty();
  }

}