package com.hkorea.skyisthelimit.analysis.exception;

import com.hkorea.skyisthelimit.common.exception.BusinessException;
import com.hkorea.skyisthelimit.common.response.ErrorCode;

public class RecommendationNotFoundException extends BusinessException {

  public RecommendationNotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }
}
