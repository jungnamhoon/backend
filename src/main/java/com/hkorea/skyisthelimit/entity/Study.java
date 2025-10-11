package com.hkorea.skyisthelimit.entity;

import com.hkorea.skyisthelimit.dto.study.request.StudyUpdateRequest;
import com.hkorea.skyisthelimit.entity.enums.ProblemRank;
import com.hkorea.skyisthelimit.entity.enums.StudyStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
public class Study {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;
  private LocalDate startDate;
  private LocalDate endDate;
  private Integer currentMemberCount;
  private Integer maxMemberCount;
  private Integer dailyProblemCount;
  private String thumbnailUrl;
  private String description;
  private Integer minLevel;
  private Integer maxLevel;
  private ProblemRank minRank;
  private ProblemRank maxRank;
  private StudyStatus status;
  private int totalSolvedProblemsCount;
  private int streak;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "creator_id", nullable = false)
  private Member creator;

  @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<MemberStudy> memberStudies = new ArrayList<>();

  @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<StudyProblem> studyProblems = new ArrayList<>();

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  private Integer problemSetterIdx;

  private LocalDate lastSubmittedDate;

  @Transient
  public StudyStatus getStatus() {
    LocalDate today = LocalDate.now();

    if (today.isBefore(startDate)) {
      return StudyStatus.BEFORE_START;
    } else if (!today.isAfter(endDate)) {
      return StudyStatus.ONGOING;
    } else {
      return StudyStatus.ENDED;
    }
  }

  public void update(StudyUpdateRequest requestDTO) {
    if (requestDTO.getName() != null && !requestDTO.getName().isBlank()) {
      this.name = requestDTO.getName();
    }
    if (requestDTO.getDescription() != null && !requestDTO.getDescription().isBlank()) {
      this.description = requestDTO.getDescription();
    }
    if (requestDTO.getStartDate() != null) {
      this.startDate = requestDTO.getStartDate();
    }
    if (requestDTO.getEndDate() != null) {
      this.endDate = requestDTO.getEndDate();
    }
    if (requestDTO.getMaxMemberCount() != null) {
      this.maxMemberCount = requestDTO.getMaxMemberCount();
    }
    if (requestDTO.getDailyProblemCount() != null) {
      this.dailyProblemCount = requestDTO.getDailyProblemCount();
    }
    if (requestDTO.getMinLevel() != null) {
      this.minLevel = requestDTO.getMinLevel();
    }
    if (requestDTO.getMaxLevel() != null) {
      this.maxLevel = requestDTO.getMaxLevel();
    }
  }

  public boolean isNotAdmin(String username) {
    return this.creator == null || !this.creator.getUsername().equals(username);
  }

  public void addMemberStudy(MemberStudy memberStudy) {
    this.memberStudies.add(memberStudy);
  }

  public void addStudyProblem(StudyProblem studyProblem) {
    this.studyProblems.add(studyProblem);
  }

  public void incrementCurrentMembers() {
    this.currentMemberCount++;
  }

}
