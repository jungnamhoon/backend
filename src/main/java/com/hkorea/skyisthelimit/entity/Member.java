package com.hkorea.skyisthelimit.entity;

import static com.hkorea.skyisthelimit.controller.TodayController.getToday;

import com.hkorea.skyisthelimit.dto.member.request.MemberUpdateRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member")
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(unique = true, nullable = false)
  private String username;
  private String name;
  private String email;
  private String role;


  private String password;
  private String profileImageUrl;
  private String nickname;
  private int score;
  private Integer ranking;
  private int totalSolvedProblems;
  private int totalReviewNotes;
  private int streak;
  private LocalDate lastSolvedDate;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private Set<MemberProblem> memberProblems = new HashSet<>();

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private Set<MemberStudy> memberStudies = new HashSet<>();

  public void update(MemberUpdateRequest requestDTO) {
    this.nickname = requestDTO.getNickname();
  }

  public void recordNewProblemSolved(MemberProblem memberProblem) {

    addMemberProblem(memberProblem);
    incrementScore(memberProblem.getProblem());
    incrementTotalSolvedProblems();
    updateStreak();
    updateLastSolvedDate();
  }

  public void recordNewProblemUnsolved(MemberProblem memberProblem) {
    addMemberProblem(memberProblem);
  }

  public void recordNotesWritten() {
    incrementTotalReviewNotes();
  }

  private void updateStreak() {

    if (lastSolvedDate == null) {
      streak = 1;
    } else if (lastSolvedDate.equals(getToday())) {
      // 같은 날에 여러번 푸는 경우 streak 변화 없음
    } else if (lastSolvedDate.plusDays(1).equals(getToday())) {
      streak += 1;
    } else {
      streak = 1;
    }
  }

  private void updateLastSolvedDate() {
    this.lastSolvedDate = getToday();
  }

  private void addMemberProblem(MemberProblem memberProblem) {
    this.memberProblems.add(memberProblem);
  }

  private void incrementScore(Problem problem) {
    this.score += problem.getLevel();
  }

  private void incrementTotalSolvedProblems() {
    this.totalSolvedProblems += 1;
  }

  private void incrementTotalReviewNotes() {
    this.totalReviewNotes += 1;
  }

}
