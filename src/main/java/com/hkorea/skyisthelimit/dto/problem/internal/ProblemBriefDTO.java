package com.hkorea.skyisthelimit.dto.problem.internal;

import java.util.List;

public record ProblemBriefDTO(
    Integer baekjoonId,
    String title,
    List<String> tags
) {}
