package com.hkorea.skyisthelimit.service;

import com.hkorea.skyisthelimit.common.exception.BusinessException;
import com.hkorea.skyisthelimit.common.response.ErrorCode;
import com.hkorea.skyisthelimit.common.utils.ImageUtils;
import com.hkorea.skyisthelimit.common.utils.QueryDSLHelper;
import com.hkorea.skyisthelimit.common.utils.mapper.MemberStudyMapper;
import com.hkorea.skyisthelimit.common.utils.mapper.StudyMapper;
import com.hkorea.skyisthelimit.common.utils.mapper.StudyProblemMapper;
import com.hkorea.skyisthelimit.dto.criteria.PageableCriteria;
import com.hkorea.skyisthelimit.dto.member.internal.MemberNotSolvingDailyProblemsDTO;
import com.hkorea.skyisthelimit.dto.problem.request.DailyProblemsCreateRequest;
import com.hkorea.skyisthelimit.dto.problem.response.DailyProblemCreateResponse;
import com.hkorea.skyisthelimit.dto.study.inner.StudyStatsDTO;
import com.hkorea.skyisthelimit.dto.study.request.StudyCreateRequest;
import com.hkorea.skyisthelimit.dto.study.request.StudyUpdateRequest;
import com.hkorea.skyisthelimit.dto.study.response.StudyCreateResponse;
import com.hkorea.skyisthelimit.dto.study.response.StudyInfoResponse;
import com.hkorea.skyisthelimit.dto.study.response.StudySummaryResponse;
import com.hkorea.skyisthelimit.dto.study.response.StudyUpdateResponse;
import com.hkorea.skyisthelimit.dto.study.response.ThumbnailUpdateResponse;
import com.hkorea.skyisthelimit.dto.studyproblem.internal.DailyProblemDTO;
import com.hkorea.skyisthelimit.dto.studyproblem.internal.StudyProblemSolvedCountByDayDTO;
import com.hkorea.skyisthelimit.dto.studyproblem.internal.StudyProblemSolvedDTO;
import com.hkorea.skyisthelimit.entity.Member;
import com.hkorea.skyisthelimit.entity.MemberStudy;
import com.hkorea.skyisthelimit.entity.Problem;
import com.hkorea.skyisthelimit.entity.QStudy;
import com.hkorea.skyisthelimit.entity.Study;
import com.hkorea.skyisthelimit.entity.StudyProblem;
import com.hkorea.skyisthelimit.entity.enums.MemberStudyStatus;
import com.hkorea.skyisthelimit.repository.StudyProblemRepository;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@Service
@RequiredArgsConstructor
public class StudyService {

  private final StudyRepository studyRepository;
  private final StudyProblemRepository studyProblemRepository;

  private final MemberService memberService;
  private final QueryDSLHelper queryDSLService;
  private final ProblemService problemService;
  private final MinioService minioService;

  @Scheduled(cron = "0 0 0 * * ?")
  @Transactional
  public void rotateProblemSetterIdx() {

    List<Study> studies = studyRepository.findAll();

    for (Study study : studies) {
      List<MemberStudy> memberStudies = study.getMemberStudies();
      int size = memberStudies.size();
      if (size == 0) {
        continue;
      }

      Integer currentIdx = study.getProblemSetterIdx();
      Integer nextIdx = (currentIdx + 1) % size;
      study.setProblemSetterIdx(nextIdx);
      studyRepository.save(study);
    }

  }

  @Transactional
  public Page<StudySummaryResponse> getStudyPage(PageableCriteria<QStudy> criteria,
      String username) {

    QStudy study = QStudy.study;

    BooleanExpression predicate = criteria.toPredicate();
    OrderSpecifier<?> orderSpecifier = criteria.toOrderSpecifier(study);
    Pageable pageable = criteria.toPageable();

    List<Study> studies = queryDSLService.fetchEntities(study, predicate, orderSpecifier,
        pageable);

    long total = queryDSLService.fetchTotalCount(study, predicate);

    List<StudySummaryResponse> studySummaryResponseList = studies.stream()
        .map(s -> {
          MemberStudyStatus memberStudyStatus = getMemberStudyStatus(s, username);
          return StudyMapper.toStudySummaryResponse(s, memberStudyStatus);
        })
        .collect(Collectors.toList());

    return new PageImpl<>(studySummaryResponseList, pageable, total);
  }

  @Transactional
  public StudyInfoResponse getStudyInfo(Integer studyId) {

    Study study = getStudy(studyId);

    StudyStatsDTO studyStatsDTO = buildStudyStatsDTO(study);

    return StudyMapper.toStudyInfoResponse(study, studyStatsDTO);
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
  public StudyCreateResponse createStudy(String username, StudyCreateRequest requestDTO)
      throws ErrorResponseException, InsufficientDataException, InternalException,
      InvalidKeyException, InvalidResponseException, IOException,
      NoSuchAlgorithmException, ServerException, XmlParserException {

    Member host = memberService.getMember(username);

    Study study = requestDTO.toEntity(host);
    studyRepository.save(study);

    if (requestDTO.getThumbnailData() != null) {
      String thumbnailUrl = saveThumbnailToMinio(requestDTO.getThumbnailData(), study);
      study.setThumbnailUrl(thumbnailUrl);
    }

    return StudyMapper.toStudyCreateResponse(study);
  }

  @Transactional
  public Set<DailyProblemCreateResponse> createDailyProblems(Integer studyId, String username,
      DailyProblemsCreateRequest requestDTO) {

    Study study = getStudy(studyId);

    validateProblemSetter(username, study);
    validateProblemCountMatch(study, requestDTO);
    validateAlreadyDailyProblemsExist(study);

    study.setLastSubmittedDate(LocalDate.now());

    return registerTodayProblems(study, requestDTO);
  }

  public Study getStudy(Integer studyId) {
    return studyRepository.findById(studyId)
        .orElseThrow(() -> new BusinessException(ErrorCode.STUDY_NOT_FOUND));
  }

  private MemberStudyStatus getMemberStudyStatus(Study study, String username) {
    return study.getMemberStudies().stream()
        .filter(ms -> ms.getMember().getUsername().equals(username))
        .map(MemberStudy::getStatus)
        .findFirst()
        .orElse(MemberStudyStatus.NONE);
  }

  private String saveThumbnailToMinio(String base64Image, Study study)
      throws ErrorResponseException, InsufficientDataException, InternalException,
      InvalidKeyException, InvalidResponseException, IOException,
      NoSuchAlgorithmException, ServerException, XmlParserException {

    String[] parts = base64Image.split(",");

    String imageString = parts[1];

    String mimeType = parts[0].split(":")[1].split(";")[0];

    byte[] decodedBytes = Base64.decodeBase64(imageString);

    return minioService.uploadImage(
        ImageType.STUDY,
        Integer.toString(study.getId()),
        decodedBytes,
        "thumbnail." + getExtension(mimeType),
        mimeType
    );
  }

  private Set<DailyProblemCreateResponse> registerTodayProblems(Study study,
      DailyProblemsCreateRequest requestDTO) {

    return requestDTO.getProblemIds()
        .stream()
        .map(problemId -> {

          Problem problem = problemService.getOrRegisterProblem(problemId);

          validateProblemLevel(study, problem);

          StudyProblem studyProblem = StudyProblem.create(study, problem);
          study.addStudyProblem(studyProblem);

          return StudyProblemMapper.toDailyProblemCreateResponse(studyProblem);
        })
        .collect(Collectors.toSet());
  }

  private String getExtension(String mimeType) {
    if (mimeType.equals("image/png")) {
      return "png";
    } else if (mimeType.equals("image/jpeg")) {
      return "jpg";
    } else {
      return "jpg";
    }
  }

  private void validateAdmin(String username, Study study) {
    if (study.isNotAdmin(username)) {
      throw new BusinessException(ErrorCode.STUDY_UPDATE_FORBIDDEN);
    }
  }

  private void validateProblemSetter(String username, Study study) {

    Integer problemSetterIdx = study.getProblemSetterIdx();

    List<MemberStudy> memberStudies = study.getMemberStudies();
    MemberStudy problemSetter = memberStudies.get(problemSetterIdx);

    if (!problemSetter.getMember().getUsername().equals(username)) {
      throw new BusinessException(ErrorCode.NOT_QUESTION_SETTER);
    }
  }

  private void validateProblemCountMatch(Study study, DailyProblemsCreateRequest requestDTO) {
    int expectedCount = study.getDailyProblemCount();
    int actualCount = requestDTO.getProblemIds() != null ? requestDTO.getProblemIds().size() : 0;

    if (expectedCount != actualCount) {
      throw new BusinessException(ErrorCode.STUDY_PROBLEM_COUNT_MISMATCH);
    }
  }

  private void validateAlreadyDailyProblemsExist(Study study) {
    if (study.getLastSubmittedDate() != null && study.getLastSubmittedDate()
        .isEqual(LocalDate.now())) {
      throw new BusinessException(ErrorCode.STUDY_PROBLEMS_ALREADY_CREATED);
    }
  }

  private void validateProblemLevel(Study study, Problem problem) {
    int problemLevel = problem.getLevel();

    if (problemLevel < study.getMinLevel() || problemLevel > study.getMaxLevel()) {
      throw new BusinessException(ErrorCode.PROBLEM_LEVEL_OUT_OF_RANGE);
    }
  }


  private StudyStatsDTO buildStudyStatsDTO(Study study) {
    
    Member dailyProblemSetter = getProblemSetter(study);

    List<StudyProblem> dailyProblemList = getDailyProblemList(study);
    List<DailyProblemDTO> dailyProblemDTOList = StudyProblemMapper.toDailyProblemDTOList(
        dailyProblemList);

    List<MemberStudy> membersNotSolvingDailyProblemsList = getMembersNotSolvingDailyProblemsList(
        study);
    List<MemberNotSolvingDailyProblemsDTO> memberNotSolvingDailyProblemsDTOList = MemberStudyMapper.toMemberNotSolvingDailyProblemsDTOList(
        membersNotSolvingDailyProblemsList);

    List<StudyProblem> studyProblemListSolvedByAll = getStudyProblemListSolvedByAll(
        study);
    List<StudyProblemSolvedDTO> studyProblemSolvedDTOList = StudyProblemMapper.toStudyProblemSolvedDTOList(
        studyProblemListSolvedByAll);

    Map<LocalDate, Integer> solvedCountMap = getSolvedCountByDate(studyProblemListSolvedByAll);
    List<StudyProblemSolvedCountByDayDTO> studyProblemSolvedCountByDayDTOList = StudyProblemMapper.toStudyProblemSolvedCountByDayDTOList(
        solvedCountMap);

    int totalSolvedProblemCount = calculateTotalSolvedProblemCount(studyProblemListSolvedByAll);

    int streak = calculateStreak(solvedCountMap, study.getDailyProblemCount());

    return StudyStatsDTO.builder()
        .dailyProblemSetterUsername(dailyProblemSetter.getUsername())
        .dailyProblemSetterProfileUrl(dailyProblemSetter.getProfileImageUrl())
        .dailyProblems(dailyProblemDTOList)
        .membersNotSolvingDailyProblems(memberNotSolvingDailyProblemsDTOList)
        .problemListSolved(studyProblemSolvedDTOList)
        .solvedCountList(studyProblemSolvedCountByDayDTOList)
        .totalSolvedProblemCount(totalSolvedProblemCount)
        .streak(streak)
        .build();
  }

  private String getProblemSetterUsername(Study study) {
    Integer problemSetterIdx = study.getProblemSetterIdx();
    return study.getMemberStudies().get(problemSetterIdx).getMember().getUsername();
  }

  private Member getProblemSetter(Study study) {
    Integer problemSetterIdx = study.getProblemSetterIdx();
    return study.getMemberStudies().get(problemSetterIdx).getMember();
  }

  private List<StudyProblem> getDailyProblemList(Study study) {
    return studyProblemRepository.findByStudyAndAssignedDate(study, LocalDate.now());
  }

  private List<MemberStudy> getMembersNotSolvingDailyProblemsList(Study study) {
    return studyRepository.findMemberStudiesNotSolvingDailyProblems(study.getId());
  }

  private List<StudyProblem> getStudyProblemListSolvedByAll(Study study) {
    return studyRepository.findStudyProblemListSolvedByAll(study.getId());
  }

  private Map<LocalDate, Integer> getSolvedCountByDate(
      List<StudyProblem> studyProblemListSolvedByAll) {
    return studyProblemListSolvedByAll.stream()
        .collect(
            Collectors.groupingBy(StudyProblem::getAssignedDate, Collectors.summingInt(e -> 1)));
  }

  private int calculateTotalSolvedProblemCount(List<StudyProblem> studyProblemListSolvedByAll) {
    return studyProblemListSolvedByAll.size();
  }

  private int calculateStreak(Map<LocalDate, Integer> dateToSolvedCountMap, int dailyProblemCount) {

    LocalDate today = LocalDate.now();
    LocalDate day = today.minusDays(1);
    int streak = 0;

    if (dateToSolvedCountMap.getOrDefault(today, 0) >= dailyProblemCount) {
      streak++;
    }

    while (true) {
      long count = dateToSolvedCountMap.getOrDefault(day, 0);

      if (count < dailyProblemCount) {
        break;
      }

      streak++;
      day = day.minusDays(1);
    }

    return streak;
  }

}