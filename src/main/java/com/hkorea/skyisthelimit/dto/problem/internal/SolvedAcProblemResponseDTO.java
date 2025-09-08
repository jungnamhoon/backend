package com.hkorea.skyisthelimit.dto.problem.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hkorea.skyisthelimit.entity.Problem;
import com.hkorea.skyisthelimit.entity.embeddable.ProblemTag;
import com.hkorea.skyisthelimit.entity.enums.ProblemRank;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class SolvedAcProblemResponseDTO {

  @JsonProperty("problemId")
  private Integer problemId;

  @JsonProperty("titleKo")
  private String titleKo;

  @JsonProperty("level")
  private Integer level;

  @JsonProperty("tags")
  private List<Tag> tags;

  public Problem toEntity() {

    List<ProblemTag> problemTagList = new ArrayList<>();

    if (this.tags == null || this.tags.isEmpty()) {
      return null;
    }

    problemTagList = this.tags.stream()
        .map(tag -> ProblemTag.builder()
            .enName(tag.getKey())
            .koName(tag.getKoName()).build())
        .toList();

    return Problem.builder()
        .baekjoonId(this.problemId)
        .title(this.titleKo)
        .url("https://www.acmicpc.net/problem/" + this.problemId)
        .rank(ProblemRank.fromLevel(this.level))
        .level(this.level)
        .problemTagList(problemTagList)
        .build();
  }

  @Data
  public static class Tag {

    private String key;
    private List<DisplayName> displayNames;

    public String getKoName() {

      return this.displayNames.stream()
          .filter(displayName -> "ko".equals(displayName.getLanguage()))
          .map(DisplayName::getName)
          .findFirst()
          .orElse(null);
    }

    @Data
    public static class DisplayName {

      private String language;
      private String name;
      @JsonProperty("short")
      private String shortName;
    }

  }


}
