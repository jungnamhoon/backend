package com.hkorea.skyisthelimit.service;

import com.hkorea.skyisthelimit.common.exception.BusinessException;
import com.hkorea.skyisthelimit.common.response.ErrorCode;
import com.hkorea.skyisthelimit.common.utils.mapper.MemberStudyMapper;
import com.hkorea.skyisthelimit.dto.memberstudy.response.MemberStudyParticipationResponse;
import com.hkorea.skyisthelimit.dto.memberstudy.response.MemberStudyResponse;
import com.hkorea.skyisthelimit.entity.Member;
import com.hkorea.skyisthelimit.entity.MemberStudy;
import com.hkorea.skyisthelimit.entity.Study;
import com.hkorea.skyisthelimit.entity.enums.MemberStudyStatus;
import com.hkorea.skyisthelimit.repository.MemberStudyRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberStudyService {

  private final MemberStudyRepository memberStudyRepository;
  private final StudyService studyService;
  private final MemberService memberService;
  private final NotificationService notificationService;

  @Transactional
  public List<MemberStudyResponse> getMemberStudyList(String username) {

    Member member = memberService.getMember(username);

    return memberStudyRepository.findByMember(member).stream()
        .filter(ms -> ms.getStatus() == MemberStudyStatus.APPROVED)
        .map(MemberStudyMapper::toMemberStudyResponse)
        .toList();
  }

  @Transactional
  public MemberStudyParticipationResponse requestJoinStudy(String username, Integer studyId) {

    Member member = memberService.getMember(username);
    Study study = studyService.getStudy(studyId);

    validateAlreadyRequested(member, study);

    MemberStudy memberStudy = MemberStudy.create(member, study, MemberStudyStatus.PENDING);
    memberStudyRepository.save(memberStudy);

    // user -> admin
    String message = member.getUsername() + " 이 스터디 참가 요청을 보냈습니다.";
    sendNotification(study.getCreator().getUsername(), message);

    return MemberStudyMapper.toMemberStudyParticipationResponse(memberStudy);
  }

  @Transactional
  public MemberStudyParticipationResponse handleRequest(Integer studyId, String username,
      String requestUsername, MemberStudyStatus newStatus) {

    Study study = studyService.getStudy(studyId);
    Member requestMember = memberService.getMember(requestUsername);

    MemberStudy memberStudy = getMemberStudy(
        requestMember, study);

    validateAdmin(memberStudy.getStudy(), username);
    validatePending(memberStudy);

    if (newStatus.equals(MemberStudyStatus.APPROVED)) {
      study.incrementCurrentMembers();
    }

    memberStudy.setStatus(newStatus);

    return MemberStudyMapper.toMemberStudyParticipationResponse(memberStudy);
  }

  @Transactional
  public MemberStudyParticipationResponse inviteToStudy(Integer studyId, String username,
      String inviteeUsername) {

    Member inviteeMember = memberService.getMember(inviteeUsername);
    Study study = studyService.getStudy(studyId);

    validateAlreadyRequested(inviteeMember, study);
    validateAdmin(study, username);

    MemberStudy memberStudy = MemberStudy.create(inviteeMember, study, MemberStudyStatus.PENDING);
    memberStudyRepository.save(memberStudy);

    String message = study.getName() + "(id=" + study.getId() + ") 의 초대 요청을 받았습니다.";
    sendNotification(inviteeUsername, message);

    return MemberStudyMapper.toMemberStudyParticipationResponse(memberStudy);
  }

  @Transactional
  public MemberStudyParticipationResponse handleInvitation(Integer studyId, String username,
      MemberStudyStatus newStatus) {

    Study study = studyService.getStudy(studyId);
    Member member = memberService.getMember(username);

    MemberStudy memberStudy = getMemberStudy(member, study);

    validatePending(memberStudy);

    if (newStatus.equals(MemberStudyStatus.APPROVED)) {
      study.incrementCurrentMembers();
    }

    memberStudy.setStatus(newStatus);

    return MemberStudyMapper.toMemberStudyParticipationResponse(memberStudy);
  }

  public MemberStudy getMemberStudy(Member member, Study study) {
    return memberStudyRepository.findByMemberAndStudy(member, study)
        .orElseThrow(() -> new BusinessException(ErrorCode.STUDY_REQUEST_NOT_FOUND));
  }

  private void validateAlreadyRequested(Member member, Study study) {
    memberStudyRepository.findByMemberAndStudy(member, study).ifPresent(ms -> {
      throw new BusinessException(ErrorCode.ALREADY_REQUESTED);
    });
  }

  private void sendNotification(String receiverUsername, String message) {

    notificationService.createNotification(receiverUsername, message);
  }

  private void validateAdmin(Study study, String username) {
    if (study.isNotAdmin(username)) {
      throw new BusinessException(ErrorCode.STUDY_ACCEPT_FORBIDDEN);
    }
  }

  private void validatePending(MemberStudy memberStudy) {
    if (memberStudy.getStatus() != MemberStudyStatus.PENDING) {
      throw new BusinessException(ErrorCode.STUDY_REQUEST_NOT_PENDING);
    }
  }

}
