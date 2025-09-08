package com.hkorea.skyisthelimit.common.exception;

import com.hkorea.skyisthelimit.common.response.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

  private final ErrorCode errorCode;

  public BusinessException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
