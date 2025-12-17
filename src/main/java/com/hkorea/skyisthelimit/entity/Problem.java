package com.hkorea.skyisthelimit.entity;

import com.hkorea.skyisthelimit.entity.embeddable.ProblemTag;
import com.hkorea.skyisthelimit.entity.enums.ProblemRank;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "problem")
public class Problem {

  @Id
  private Integer baekjoonId;

  private String title;
  private String url;
  @Column(name = "`rank`")
  @Enumerated(EnumType.STRING)
  private ProblemRank rank;
  private Integer level;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "problem_tags", joinColumns = @JoinColumn(name = "problem_id"))
  @BatchSize(size = 10)
  private List<ProblemTag> problemTagList;

  @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private Set<MemberProblem> memberProblems = new HashSet<>();

}