package com.hkorea.skyisthelimit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class WrongReason {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="member_problem_id")
  private MemberProblem memberProblem;

  private String reasonText;

  public WrongReason(MemberProblem memberProblem, String reasonText) {
    this.memberProblem = memberProblem;
    this.reasonText = reasonText;
  }
}
