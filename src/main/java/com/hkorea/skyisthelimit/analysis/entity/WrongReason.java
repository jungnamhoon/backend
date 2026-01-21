package com.hkorea.skyisthelimit.analysis.entity;

import com.hkorea.skyisthelimit.entity.MemberProblem;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class WrongReason {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_problem_id")
  private MemberProblem memberProblem;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "weakness_id")
  private Weakness weakness;

  private String reason;

  public WrongReason(MemberProblem memberProblem, Weakness weakness, String reason) {
    this.memberProblem = memberProblem;
    this.weakness = weakness;
    this.reason = reason;
  }

  public Integer getProblemId() {
    if (this.memberProblem == null || this.memberProblem.getProblem() == null) {
      return null;
    }
    return this.memberProblem.getProblem().getBaekjoonId();
  }

}
