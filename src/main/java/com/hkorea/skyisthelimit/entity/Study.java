package com.hkorea.skyisthelimit.entity;

import static com.hkorea.skyisthelimit.controller.TodayController.getToday;

import com.hkorea.skyisthelimit.dto.study.request.StudyUpdateRequest;
import com.hkorea.skyisthelimit.entity.embeddable.DailyProblem;
import com.hkorea.skyisthelimit.entity.enums.ProblemRank;
import com.hkorea.skyisthelimit.entity.enums.StudyProblemStatus;
import com.hkorea.skyisthelimit.entity.enums.StudyStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
  private int totalSolvedProblemsCount = 0;
  private int streak = 0;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "creator_id", nullable = false)
  private Member creator;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "daily_problems_setter_id")
  private Member dailyProblemsSetter;

  @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private Set<MemberStudy> memberStudies = new HashSet<>();

  @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<StudyProblem> studyProblems = new ArrayList<>();

  @ElementCollection
  @CollectionTable(name = "study_daily_problems", joinColumns = @JoinColumn(name = "study_id"))
  private Set<DailyProblem> dailyProblems = new HashSet<>();

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "last_streak_updated_date")
  private LocalDate lastStreakUpdatedDate;

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
    if (requestDTO.getThumbnailUrl() != null && !requestDTO.getThumbnailUrl().isBlank()) {
      this.thumbnailUrl = requestDTO.getThumbnailUrl();
    }
    if (requestDTO.getMinLevel() != null) {
      this.minLevel = requestDTO.getMinLevel();
    }
    if (requestDTO.getMaxLevel() != null) {
      this.maxLevel = requestDTO.getMaxLevel();
    }
  }

  public void updateStreak() {

    LocalDate today = getToday();

    // 오늘 이미 streak가 계산되었으면 return
    if (lastStreakUpdatedDate != null && lastStreakUpdatedDate.equals(today)) {
      return;
    }
    incrementStreak();
    lastStreakUpdatedDate = getToday();
  }

  public boolean isNotAdmin(String username) {
    return this.creator == null || !this.creator.getUsername().equals(username);
  }

  public boolean isNotQuestionSetter(String username) {
    return this.dailyProblemsSetter == null || !this.dailyProblemsSetter.getUsername()
        .equals(username);
  }

  public boolean isDailyProblemsUpToDate() {
    LocalDate today = getToday();
    return dailyProblems.stream()
        .anyMatch(p -> p.getAssignedDate().equals(today));
  }

  public boolean isDailyProblemsNotUpToDate() {
    return !isDailyProblemsUpToDate();
  }

  public void clearDailyProblems() {
    this.dailyProblems.clear();
  }

  public void addMemberStudy(MemberStudy memberStudy) {
    this.memberStudies.add(memberStudy);
  }

  public void addStudyProblem(StudyProblem studyProblem) {
    this.studyProblems.add(studyProblem);
  }

  public void addDailyProblem(DailyProblem dailyProblem) {
    this.dailyProblems.add(dailyProblem);
  }

  public void incrementCurrentMembers() {
    this.currentMemberCount++;
  }

  public List<StudyProblem> getSolvedStudyProblemList() {
    return studyProblems.stream()
        .filter(sp -> sp.getStatus() == StudyProblemStatus.SOLVED)
        .toList();
  }

  private void incrementStreak() {
    this.streak++;
  }

}
