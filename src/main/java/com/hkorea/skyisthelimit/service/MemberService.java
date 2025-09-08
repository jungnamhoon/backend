package com.hkorea.skyisthelimit.service;

import com.hkorea.skyisthelimit.common.exception.BusinessException;
import com.hkorea.skyisthelimit.common.response.ErrorCode;
import com.hkorea.skyisthelimit.common.utils.mapper.MemberMapper;
import com.hkorea.skyisthelimit.dto.member.request.MemberUpdateRequest;
import com.hkorea.skyisthelimit.dto.member.response.MemberInfoResponse;
import com.hkorea.skyisthelimit.dto.member.response.MemberUpdateResponse;
import com.hkorea.skyisthelimit.entity.Member;
import com.hkorea.skyisthelimit.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;

  @Transactional
  public MemberInfoResponse getMemberInfo(String username) {

    Member member = getMember(username);

    return MemberMapper.toMemberInfoResponse(member);
  }

  @Transactional
  public MemberUpdateResponse updateMember(String username, MemberUpdateRequest requestDTO) {

    Member member = getMember(username);
    member.update(requestDTO);

    return MemberMapper.toMemberUpdateResponse(member);
  }

  public Member getMember(String username) {
    return memberRepository.findByUsername(username)
        .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
  }
}
