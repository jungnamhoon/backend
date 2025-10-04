package com.hkorea.skyisthelimit.entity;

import com.hkorea.skyisthelimit.entity.enums.StudyProblemStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

@Entity
@Getter
@Table(name = "study_problem")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyProblem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "study_id")
  private Study study;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "problem_id")
  private Problem problem;

  @Column(name = "assigned_date")
  private LocalDate assignedDate;

  @Column(name = "solved_date")
  private LocalDate solvedDate;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private StudyProblemStatus status;


  public static StudyProblem create(Study study, Problem problem) {
    return StudyProblem.builder()
        .study(study)
        .problem(problem)
        .assignedDate(LocalDate.now())
        .status(StudyProblemStatus.UNSOLVED)
        .build();
  }

  public void markToSolved() {
    this.status = StudyProblemStatus.SOLVED;
    this.solvedDate = LocalDate.now();
  }
}