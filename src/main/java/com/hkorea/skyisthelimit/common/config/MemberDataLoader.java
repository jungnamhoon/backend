package com.hkorea.skyisthelimit.common.config;

import com.hkorea.skyisthelimit.common.utils.PasswordUtils;
import com.hkorea.skyisthelimit.entity.Member;
import com.hkorea.skyisthelimit.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberDataLoader implements CommandLineRunner {

  private final MemberRepository memberRepository;

  @Override
  public void run(String... args) throws Exception {
    List<Member> members = new ArrayList<>();

    for (int i = 1; i <= 100; i++) {
      Member member = Member.builder()
          .username("user" + i)
          .password(PasswordUtils.encode("Password12@"))
          .role("ROLE_USER")
          .nickname("nickname" + i)
          .build();

      members.add(member);
    }

    memberRepository.saveAll(members);
    log.info("100명의 테스트 회원 생성 완료");
  }
}
