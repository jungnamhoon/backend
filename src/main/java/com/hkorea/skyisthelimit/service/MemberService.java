package com.hkorea.skyisthelimit.service;

import com.hkorea.skyisthelimit.common.exception.BusinessException;
import com.hkorea.skyisthelimit.common.response.ErrorCode;
import com.hkorea.skyisthelimit.common.utils.mapper.MemberMapper;
import com.hkorea.skyisthelimit.common.utils.mapper.MemberProblemMapper;
import com.hkorea.skyisthelimit.dto.member.internal.MemberStatsDTO;
import com.hkorea.skyisthelimit.dto.member.request.MemberUpdateRequest;
import com.hkorea.skyisthelimit.dto.member.response.MemberInfoResponse;
import com.hkorea.skyisthelimit.dto.member.response.MemberUpdateResponse;
import com.hkorea.skyisthelimit.dto.memberproblem.internal.MemberProblemSolvedCountByDayDTO;
import com.hkorea.skyisthelimit.dto.memberproblem.internal.MemberProblemSolvedDTO;
import com.hkorea.skyisthelimit.entity.Member;
import com.hkorea.skyisthelimit.entity.MemberProblem;
import com.hkorea.skyisthelimit.entity.enums.MemberProblemStatus;
import com.hkorea.skyisthelimit.repository.MemberRepository;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final MinioClient minioClient;

  @Value("${minio.bucket}")
  private String bucketName;

  @Transactional
  public MemberInfoResponse getMemberInfo(String username) {

    Member member = getMember(username);

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
  public String updateProfileImage(String username, MultipartFile profileImage) throws Exception {
    Member member = getMember(username);

    if (profileImage != null && !profileImage.isEmpty()) {
      String objectName = "profile/"
          + username + "_"
          + Instant.now().toEpochMilli() + "_"
          + profileImage.getOriginalFilename();

      minioClient.putObject(
          PutObjectArgs.builder()
              .bucket(bucketName)
              .object(objectName)
              .stream(profileImage.getInputStream(), profileImage.getSize(), -1)
              .contentType(profileImage.getContentType())
              .build()
      );

      String imageUrl = bucketName + "/" + objectName;
      member.setProfileImageUrl(imageUrl);
      return imageUrl;
    }

    return member.getProfileImageUrl();
  }


  public Member getMember(String username) {
    return memberRepository.findByUsername(username)
        .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
  }

  private MemberStatsDTO buildMemberStatsDTO(Member member) {
    // 랭킹
    Integer ranking = calculateRanking(member);

    // 푼 문제 리스트
    List<MemberProblemSolvedDTO> memberProblemSolvedDTOList = calculateSolvedProblemList(
        member.getMemberProblems());

    // 날짜별 푼 문제 개수
    List<MemberProblemSolvedCountByDayDTO> memberProblemSolvedCountByDayDTOList = calculateSolvedCountList(
        member.getMemberProblems());

    return MemberMapper.toMemberStatsDTO(
        ranking, memberProblemSolvedDTOList, memberProblemSolvedCountByDayDTOList);
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
      Set<MemberProblem> memberProblems) {

    // 메인 로직
    Map<LocalDate, Integer> dateToSolvedCountMap = memberProblems.stream()
        .filter(mp -> mp.getSolvedDate() != null)
        .collect(Collectors.groupingBy(
            MemberProblem::getSolvedDate,
            Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
        ));

    return MemberProblemMapper.toMemberProblemSolvedCountByDayDTOList(dateToSolvedCountMap);
  }


}
