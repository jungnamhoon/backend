package com.hkorea.skyisthelimit.common.exception;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.hkorea.skyisthelimit.common.response.ApiResponse;
import com.hkorea.skyisthelimit.common.response.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  // 비즈니스 예외 처리
  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ApiResponse<String>> handleBusinessException(BusinessException e) {
    log.error("비지니스 예외: {}", e.getMessage());
    return ApiResponse.of(e.getErrorCode());
  }

  // DTO 검증 예외 처리 (javax/validation)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<List<String>>> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {

    List<String> details = e.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
        .toList();

    log.warn("DTO 검증 예외: {}", details);
    return ApiResponse.of(ErrorCode.VALIDATION_FAILED, details);
  }

  // JSON 읽기 실패 처리
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiResponse<String>> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException e) {

    Throwable cause = e.getCause();

    // 타입 불일치 (예: LocalDate, Integer, Enum 등)
    if (cause instanceof InvalidFormatException ife) {

      String field = ife.getPath().stream()
          .map(Reference::getFieldName)
          .filter(name -> name != null && !name.isEmpty())
          .reduce((a, b) -> a + "." + b)
          .orElse("unknown");

      String targetType = ife.getTargetType().getSimpleName();
      String value = ife.getValue() != null ? ife.getValue().toString() : "null";

      String message = String.format(
          "필드 '%s'의 값 '%s'이(가) 올바르지 않습니다. 기대 타입: %s",
          field, value, targetType
      );

      log.warn("Request body 타입 오류: {}", message);
      return ApiResponse.of(ErrorCode.INVALID_FORMAT, message);
    }

    // JSON 문법 오류, 매핑 오류
    log.warn("Malformed JSON 요청: {}", e.getMessage());
    return ApiResponse.of(ErrorCode.MALFORMED_JSON);
  }

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ApiResponse<String>> handleException(Exception e,
      HttpServletRequest request) throws Exception {
    log.error("처리되지 않은 예외: ", e);
    return ApiResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
  }

}
