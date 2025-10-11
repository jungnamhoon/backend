package com.hkorea.skyisthelimit.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {


  UNAUTHORIZED(HttpStatus.BAD_REQUEST, "C001", "회원 정보가 올바르지 않습니다"),
  VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "C002", "요청 데이터 검증에 실패했습니다"),
  INVALID_FORMAT(HttpStatus.BAD_REQUEST, "C003", "값 형식이 올바르지 않습니다"),
  MALFORMED_JSON(HttpStatus.BAD_REQUEST, "C004", "JSON 형식이 올바르지 않습니다"),
  NOT_FOUND(HttpStatus.BAD_REQUEST, "C005", "해당 리소스를 찾을 수 없습니다"),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C006", "서버 내부 오류가 발생했습니다"),

  REFRESH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "A001", "리프레시 토큰이 유효하지 않습니다"),
  REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "A002", "리프레시 토큰이 만료되었습니다"),
  REFRESH_TOKEN_CATEGORY_INVALID(HttpStatus.UNAUTHORIZED, "A003", "리프레시 토큰이 아닙니다"),

  DUPLICATE_MEMBER(HttpStatus.BAD_REQUEST, "M001", "이미 존재하는 회원입니다"),
  DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "M002", "이미 존재하는 닉네임입니다"),
  MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "M003", "멤버를 찾을 수 없습니다"),
  INVALID_IMAGE_SIZE(HttpStatus.BAD_REQUEST, "M004", "잘못된 이미지 사이즈입니다"),
  INVALID_IMAGE_MIME_TYPE(HttpStatus.BAD_REQUEST, "M005", "잘못된 이미지 MIME_TYPE 입니다"),
  INVALID_IMAGE_RESOLUTION(HttpStatus.BAD_REQUEST, "M006", "잘못된 이미지 RESOLUTION 입니다"),

  MEMBER_PROBLEM_NOT_FOUND(HttpStatus.BAD_REQUEST, "MP001", "회원의 문제를 찾을 수 없습니다"),

  PROBLEM_NOT_FOUND(HttpStatus.BAD_REQUEST, "P001", "문제를 찾을 수 없습니다"),
  UNSUPPORTED_SORT_FIELD(HttpStatus.BAD_REQUEST, "P002", "지원하지 않는 정렬 필드입니다"),

  STUDY_NOT_RECRUITING(HttpStatus.BAD_REQUEST, "S001", "스터디 모집 중이 아닙니다"),
  ALREADY_REQUESTED(HttpStatus.BAD_REQUEST, "S002", "이미 요청(참가신청/초대)을 보냈습니다"),
  STUDY_NOT_FOUND(HttpStatus.BAD_REQUEST, "S003", "스터디를 찾을 수 없습니다"),
  STUDY_UPDATE_FORBIDDEN(HttpStatus.FORBIDDEN, "S004", "스터디를 수정할 권한이 없습니다"),
  STUDY_NOT_ACTIVE(HttpStatus.BAD_REQUEST, "S005", "스터디가 진행중이 아닙니다"),
  STUDY_PROBLEMS_ALREADY_CREATED(HttpStatus.BAD_REQUEST, "S006", "이미 문제가 출제되었습니다"),

  STUDY_REQUEST_NOT_FOUND(HttpStatus.BAD_REQUEST, "MS001", "스터디 참가 요청을 찾을 수 없습니다"),
  STUDY_ACCEPT_FORBIDDEN(HttpStatus.BAD_REQUEST, "MS002", "방장만 요청을 수락/거절할 수 있습니다"),
  STUDY_INVITE_FORBIDDEN(HttpStatus.BAD_REQUEST, "MS003", "방장만 초대를 보낼 수 있습니다"),
  STUDY_REQUEST_NOT_PENDING(HttpStatus.BAD_REQUEST, "MS003", "대기 중인 요청이 아닙니다"),
  NOT_QUESTION_SETTER(HttpStatus.BAD_REQUEST, "MS004", "오늘의 문제 출제자가 아닙니다"),
  DUPLICATE_TODAY_PROBLEM(HttpStatus.BAD_REQUEST, "MS005", "오늘의 문제는 이미 출제되었습니다"),

  STUDY_PROBLEM_COUNT_MISMATCH(HttpStatus.BAD_REQUEST, "SP001",
      "요청된 문제 개수가 스터디에서 설정된 오늘의 문제 개수와 다릅니다. 올바른 개수로 다시 제출해주세요"),
  PROBLEM_LEVEL_OUT_OF_RANGE(HttpStatus.BAD_REQUEST, "SP002", "출제 문제 난이도가 맞지 않습니다.");


  private final HttpStatus httpStatus;
  private final String code;
  private final String message;

  public int getStatusCode() {
    return httpStatus.value();
  }
}
