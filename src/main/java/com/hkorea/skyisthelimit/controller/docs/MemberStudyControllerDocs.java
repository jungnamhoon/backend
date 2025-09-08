package com.hkorea.skyisthelimit.controller.docs;

import com.hkorea.skyisthelimit.common.response.ApiResponse;
import com.hkorea.skyisthelimit.dto.memberstudy.response.MemberStudyResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

@Tag(name = "MemberStudy", description = "회원이 참여하고 있는 스터디 관련 API - JWT 토큰 필요")
public interface MemberStudyControllerDocs {

  @Operation(
      summary = "내가 참여한 스터디 조회",
      description = "로그인한 사용자가 참여하고 있는 스터디 목록을 조회합니다."
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200",
          description = "참여 스터디 목록 조회 성공"
      )
  })
  ResponseEntity<ApiResponse<List<MemberStudyResponse>>> getMyStudyList(
      @AuthenticationPrincipal Jwt token
  );

}
