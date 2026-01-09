package com.hkorea.skyisthelimit.dto.prompt;

import java.util.List;

public record IncorrectSummaryDTO(
    String problemId,
    String description,
    String inputSpec,
    String outputSpec,
    String sourceCode,
    List<String> tagNames
) {}
