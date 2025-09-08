package com.hkorea.skyisthelimit.dto.problem.request;

import java.util.List;
import lombok.Data;

@Data
public class DailyProblemsCreateRequest {

  private List<Integer> problemIds;
}
