package com.hkorea.skyisthelimit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class WrongReason {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="member_problem_id")
  private MemberProblem memberProblem;

  private String reasonText;

  @Lob
  @Column(columnDefinition = "TEXT")
  private String embeddingJson;

  public WrongReason(MemberProblem memberProblem, String reasonText, String embeddingJson) {
    this.memberProblem = memberProblem;
    this.reasonText = reasonText;
    this.embeddingJson = embeddingJson;
  }
}
