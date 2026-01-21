package com.hkorea.skyisthelimit.analysis.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.hkorea.skyisthelimit.analysis.dto.request.AnalysisCriteria;
import com.hkorea.skyisthelimit.analysis.entity.Weakness;
import com.hkorea.skyisthelimit.analysis.entity.WrongReason;
import com.hkorea.skyisthelimit.common.config.TestConfig;
import com.hkorea.skyisthelimit.entity.Member;
import com.hkorea.skyisthelimit.entity.MemberProblem;
import com.hkorea.skyisthelimit.entity.Problem;
import com.hkorea.skyisthelimit.entity.embeddable.ProblemTag;
import com.hkorea.skyisthelimit.entity.enums.MemberProblemStatus;
import com.hkorea.skyisthelimit.entity.enums.ProblemRank;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({TestConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class WeaknessRepositoryTest {

  @Autowired
  private WeaknessRepository weaknessRepository;

  @Autowired
  private EntityManager em;

  private int problemSeq = 1000;


  @Test
  @DisplayName("조건에 맞는 문제와 연관된 취약점 중 빈도수 순으로 TOP N 조회")
  void findTopWeaknesses() {

    // =========================
    // Given
    // =========================
    // 1. Member 생성
    Member member = persistMember();

    // 2. 문제 생성 (필터링 대조군)
    List<Problem> highLevelBruteForceProblems = persistProblems(10, 10, List.of(createTag("BRUTE_FORCE", "브루트포스")));
    List<Problem> highLevelDpProblems = persistProblems(10, 10, List.of(createTag("DP", "다이나믹 프로그래밍")));
    List<Problem> lowLevelDpProblems = persistProblems(10, 1, List.of(createTag("DP", "다이나믹 프로그래밍")));

    // 3. 약점 데이터 생성 (태그별로 이름을 다르게 지어 "힌트" 생성)
    List<Weakness> highLevelDpWeaknesses = persistWeaknesses(5, "HIGH_DP");
    List<Weakness> lowLevelDpWeaknesses = persistWeaknesses(5, "LOW_DP");
    List<Weakness> bruteWeaknesses = persistWeaknesses(5, "BRUTE");

    // 4. MemberProblem 및 WrongReason 연결 (핵심: 문제 성격에 맞는 약점 연결)
    // A. 브루트포스 문제들 -> 브루트포스 약점들 연결 (❌ 태그 필터에 걸려야 함)
    for (Problem p : highLevelBruteForceProblems) {
      MemberProblem mp = persistMemberProblem(member, p);
      linkWeaknessesToMP(mp, bruteWeaknesses, 3);
    }

    // B. 높은 레벨 DP 문제들 -> DP 약점들 연결 (✅ 이게 결과에 나와야 함)
    for (Problem p : highLevelDpProblems) {
      MemberProblem mp = persistMemberProblem(member, p);
      linkWeaknessesToMP(mp, highLevelDpWeaknesses, 3);
    }

    // C. 낮은 레벨 DP 문제들 -> DP 약점들 연결 (❌ 레벨 필터에 걸려야 함)
    for (Problem p : lowLevelDpProblems) {
      MemberProblem mp = persistMemberProblem(member, p);
      linkWeaknessesToMP(mp, lowLevelDpWeaknesses, 3);
    }

    em.flush();
    em.clear();

    // =========================
    // When
    // =========================
    AnalysisCriteria criteria = AnalysisCriteria.builder()
        .levelStart(5)
        .levelEnd(15)
        .tags(List.of("다이나믹 프로그래밍"))
        .build();

    List<Weakness> result = weaknessRepository.findTopWeaknesses(member.getUsername(), criteria, 5);

    // =========================
    // Then
    // =========================
    // 1. 결과 검증: DP 관련 약점만 포함되어야 하며, BRUTE 관련은 없어야 함
    assertThat(result).isNotEmpty();

    // 2. 필터링 조건 (태그 + 레벨) 검증
    assertThat(result).allSatisfy(w -> {
      // [태그 검증] "DP"라는 키워드가 포함되어 있어야 함
      assertThat(w.getWeaknessSummary())
          .as("조회된 약점 [%s]은 '다이나믹 프로그래밍' 태그와 연관되어야 함", w.getWeaknessSummary())
          .contains("DP");

      // [레벨 검증] "HIGH" 키워드는 있고, "LOW"는 없어야 함 (Level 5~10 필터링 확인)
      assertThat(w.getWeaknessSummary())
          .as("조회된 약점 [%s]은 설정된 레벨 범위(5~10)에 속해야 함 (LOW_DP 포함 금지)", w.getWeaknessSummary())
          .contains("HIGH")
          .doesNotContain("LOW");

      // [제외 조건 검증] 브루트포스 관련 약점은 절대 없어야 함
      assertThat(w.getWeaknessSummary())
          .as("조회된 약점 [%s]은 검색 조건에서 제외된 '브루트포스' 관련 데이터가 아니어야 함", w.getWeaknessSummary())
          .doesNotContain("BRUTE");
    });

    // 4. 빈도수 내림차순 정렬 검증
    assertThat(result).isSortedAccordingTo(
        Comparator.comparingInt(Weakness::getFrequency).reversed()
    );
  }

  // ==================================================
  // Helper Methods
  // ==================================================
  private Member persistMember() {
    Member member = Member.builder()
        .username("tester")
        .oauth2Username("oauth2_tester")
        .build();
    em.persist(member);
    return member;
  }

  private List<Problem> persistProblems(
      int count,
      int level,
      List<ProblemTag> tags
  ) {
    List<Problem> problems = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      problems.add(persistProblem(level, tags));
    }
    return problems;
  }

  private Problem persistProblem(
      int level,
      List<ProblemTag> tags
  ) {
    int id = problemSeq++;

    Problem problem = Problem.builder()
        .baekjoonId(id)
        .title("테스트 문제 " + id)
        .level(level)
        .rank(ProblemRank.fromLevel(level))
        .url("https://www.acmicpc.net/problem/" + id)
        .problemTagList(tags)
        .build();

    em.persist(problem);
    return problem;
  }

  private List<MemberProblem> persistMemberProblems(
      Member member,
      List<Problem>... problemGroups
  ) {
    List<MemberProblem> result = new ArrayList<>();

    for (List<Problem> problems : problemGroups) {
      for (Problem problem : problems) {
        MemberProblem mp =
            MemberProblem.create(member, problem, MemberProblemStatus.UNSOLVED);
        em.persist(mp);
        result.add(mp);
      }
    }

    return result;
  }

  private MemberProblem persistMemberProblem(Member member, Problem problem) {
    // 엔티티에 정의된 정적 팩토리 메서드 사용
    MemberProblem memberProblem = MemberProblem.create(
        member,
        problem,
        MemberProblemStatus.UNSOLVED
    );

    // JPA 영속성 컨텍스트에 저장
    em.persist(memberProblem);

    return memberProblem;
  }

  private List<Weakness> persistWeaknesses(int count, String prefix) {
    List<Weakness> weaknesses = new ArrayList<>();
    for (int i = 1; i <= count; i++) {
      Weakness weakness =
          new Weakness(prefix + "_취약점" + i, i, new float[1536]);
      em.persist(weakness);
      weaknesses.add(weakness);
    }
    return weaknesses;
  }

  private void linkWeaknessesToMP(MemberProblem mp, List<Weakness> weaknesses, int count) {
    for (int i = 0; i < count; i++) {
      Weakness w = weaknesses.get(i % weaknesses.size());
      em.persist(new WrongReason(mp, w, "오답분석"));
    }
  }

  private ProblemTag createTag(String en, String ko) {
    return ProblemTag.builder().enName(en).koName(ko).build();
  }

}
