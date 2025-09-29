package com.hkorea.skyisthelimit.dto.member.internal;

import com.hkorea.skyisthelimit.dto.memberproblem.internal.MemberProblemSolvedCountByDayDTO;
import com.hkorea.skyisthelimit.dto.memberproblem.internal.MemberProblemSolvedDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberStatsDTO {

  private Integer ranking;
  private List<MemberProblemSolvedDTO> memberProblemSolvedDTOList;
  private List<MemberProblemSolvedCountByDayDTO> memberProblemSolvedCountByDayDTOList;
  private int streak;
}
