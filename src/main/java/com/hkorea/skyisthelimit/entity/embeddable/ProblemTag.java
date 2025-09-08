package com.hkorea.skyisthelimit.entity.embeddable;

import com.hkorea.skyisthelimit.dto.memberproblem.response.MemberProblemResponse;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Embeddable
@Getter
@AllArgsConstructor
@Builder
public class ProblemTag {

  private String enName;
  private String koName;

  protected ProblemTag() {
  }

  public MemberProblemResponse.ProblemTagDTO toDTO() {
    return new MemberProblemResponse.ProblemTagDTO(enName, koName);
  }
}
