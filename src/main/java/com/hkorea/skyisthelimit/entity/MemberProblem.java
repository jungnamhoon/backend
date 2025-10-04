package com.hkorea.skyisthelimit.entity;

import com.hkorea.skyisthelimit.entity.enums.MemberProblemStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
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
@Table(name = "member_problem")
public class MemberProblem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private LocalDate solvedDate;
  private int solvedCount;
  private MemberProblemStatus status;

  @Builder.Default
  private Boolean noteWritten = false;
  private String note;
  private Long lastSubmitId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "problem_id")
  private Problem problem;

  public static MemberProblem create(Member member, Problem problem, MemberProblemStatus status) {

    return MemberProblem.builder()
        .member(member)
        .problem(problem)
        .status(status)
        .solvedDate(status == MemberProblemStatus.SOLVED ? LocalDate.now() : null)
        .solvedCount(0)
        .build();
  }

  public void writeNote(String content) {

    this.note = content;
    this.noteWritten = Boolean.TRUE;
  }

  public void incrementSolvedCount() {
    this.solvedCount += 1;
  }

  public void setSolvedDate(LocalDate solvedDate) {
    this.solvedDate = (solvedDate != null) ? solvedDate : LocalDate.now();
  }

}