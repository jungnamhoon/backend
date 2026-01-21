package com.hkorea.skyisthelimit.analysis.dto.internal;

import java.util.List;

public record ProblemRecommendContext(
    Integer baekjoonId,
    List<String> tags,
    String wrongReason
) {

}
