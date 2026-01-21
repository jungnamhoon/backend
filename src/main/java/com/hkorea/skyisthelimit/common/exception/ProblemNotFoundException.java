package com.hkorea.skyisthelimit.common.exception;

import com.hkorea.skyisthelimit.common.response.ErrorCode;

public class ProblemNotFoundException extends BusinessException {

  public ProblemNotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }
}
