package com.hkorea.skyisthelimit.dto.prompt;

import java.util.List;

public record ProblemRecommendDTO(
    Integer baekjoonId,
    List<String> tags,
    String wrongReason
) {

}
