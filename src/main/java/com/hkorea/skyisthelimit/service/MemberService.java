package com.hkorea.skyisthelimit.service;

import com.hkorea.skyisthelimit.common.exception.BusinessException;
import com.hkorea.skyisthelimit.common.response.ErrorCode;
import com.hkorea.skyisthelimit.common.utils.ImageUtils;
import com.hkorea.skyisthelimit.common.utils.mapper.MemberMapper;
import com.hkorea.skyisthelimit.common.utils.mapper.MemberProblemMapper;
import com.hkorea.skyisthelimit.dto.member.internal.MemberStatsDTO;
import com.hkorea.skyisthelimit.dto.member.request.MemberUpdateRequest;
import com.hkorea.skyisthelimit.dto.member.response.MemberInfoResponse;
import com.hkorea.skyisthelimit.dto.member.response.MemberUpdateResponse;
import com.hkorea.skyisthelimit.dto.member.response.ProfileUpdateResponse;
import com.hkorea.skyisthelimit.dto.memberproblem.internal.MemberProblemSolvedCountByDayDTO;
import com.hkorea.skyisthelimit.dto.memberproblem.internal.MemberProblemSolvedDTO;
import com.hkorea.skyisthelimit.entity.Member;
import com.hkorea.skyisthelimit.entity.MemberProblem;
import com.hkorea.skyisthelimit.entity.enums.MemberProblemStatus;
import com.hkorea.skyisthelimit.repository.MemberRepository;
import com.hkorea.skyisthelimit.service.enums.ImageType;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final MinioService minioService;

  @Transactional
  public MemberInfoResponse getMemberInfo(String username) {

    Member member = getMemberWithProblems(username);

    MemberStatsDTO statsDTO = buildMemberStatsDTO(member);

    return MemberMapper.toMemberInfoResponse(member, statsDTO);
  }

  @Transactional
  public MemberUpdateResponse updateMember(String username, MemberUpdateRequest requestDTO) {

    Member member = getMember(username);
    member.update(requestDTO);

    return MemberMapper.toMemberUpdateResponse(member);
  }

  @Transactional
  public ProfileUpdateResponse updateProfileImage(String username, MultipartFile profileImage)
      throws ErrorResponseException, InsufficientDataException, InternalException,
      InvalidKeyException, InvalidResponseException, IOException, NoSuchAlgorithmException,
      ServerException, XmlParserException {

    Member member = getMember(username);

    if (profileImage == null || profileImage.isEmpty()) {
      return MemberMapper.toProfileUpdateResponse(member.getProfileImageUrl());
    }

    // 1. 기존 이미지 삭제
    minioService.deleteOldImage(member.getProfileImageUrl());

    // 2. 이미지 검증
    ImageUtils.validateImage(profileImage);

    // 3. 썸네일 생성
    byte[] thumbnail = ImageUtils.createThumbnail(profileImage);

    // 4. 파일 업로드
    String imageUrl = minioService.uploadImage(
        ImageType.PERSONAL, member.getUsername(), thumbnail, profileImage.getOriginalFilename(),
        profileImage.getContentType());

    member.setProfileImageUrl(imageUrl);

    return MemberMapper.toProfileUpdateResponse(imageUrl);
  }

  public Member getMember(String username) {
    return memberRepository.findByUsername(username)
        .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
  }

  public Member getMemberWithProblems(String username) {
    return memberRepository.findByUsernameWithProblems(username)
        .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
  }

  public Member getMemberByOauth2Username(String oauth2Username) {
    return memberRepository.findByOauth2Username(oauth2Username)
        .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
  }

  private MemberStatsDTO buildMemberStatsDTO(Member member) {
    // 랭킹 - 문제 없음
    Integer ranking = calculateRanking(member);

    // 푼 문제 리스트
    List<MemberProblemSolvedDTO> memberProblemSolvedDTOList = calculateSolvedProblemList(
        member.getMemberProblems()); // N+1 문제 발생

    Set<MemberProblem> memberProblems = member.getMemberProblems();

    Map<LocalDate, Integer> dateToSolvedCountMap = memberProblems.stream()
        .filter(mp -> mp.getSolvedDate() != null)
        .collect(Collectors.groupingBy(
            MemberProblem::getSolvedDate,
            Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
        ));

    // 날짜별 푼 문제 개수
    List<MemberProblemSolvedCountByDayDTO> memberProblemSolvedCountByDayDTOList = calculateSolvedCountList(
        dateToSolvedCountMap);

    // 연속 학습
    int streak = calculateStreak(dateToSolvedCountMap);

    return MemberMapper.toMemberStatsDTO(
        ranking, streak, memberProblemSolvedDTOList, memberProblemSolvedCountByDayDTOList);
  }


  private Integer calculateRanking(Member member) {

    if (member.getScore() == 0) {
      return null;
    }
    return memberRepository.findRanking(member.getScore());
  }

  private List<MemberProblemSolvedDTO> calculateSolvedProblemList(
      Set<MemberProblem> memberProblems) {

    // 메인 로직
    List<MemberProblem> memberProblemSolvedList = memberProblems.stream()
        .filter(mp ->
            mp.getStatus() == MemberProblemStatus.SOLVED
                || mp.getStatus() == MemberProblemStatus.MULTI_TRY).toList();

    return MemberProblemMapper.toMemberProblemSolvedDTOList(memberProblemSolvedList);
  }

  private List<MemberProblemSolvedCountByDayDTO> calculateSolvedCountList(
      Map<LocalDate, Integer> dateToSolvedCountMap) {
    return MemberProblemMapper.toMemberProblemSolvedCountByDayDTOList(dateToSolvedCountMap);
  }

  private int calculateStreak(Map<LocalDate, Integer> dateToSolvedCountMap) {

    LocalDate today = LocalDate.now();
    LocalDate day = today.minusDays(1);
    int streak = 0;

    if (dateToSolvedCountMap.containsKey(today)) {
      streak++;
    }

    while (true) {
      int count = dateToSolvedCountMap.getOrDefault(day, 0);

      if (count == 0) {
        break;
      }
      streak++;
      day = day.minusDays(1);
    }

    return streak;

  }


}
