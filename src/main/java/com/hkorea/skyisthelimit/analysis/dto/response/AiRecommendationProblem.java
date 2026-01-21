package com.hkorea.skyisthelimit.analysis.dto.response;

public record AiRecommendationProblem(
    Long problemId,
    String problemUrl,
    String reason
) {

}
