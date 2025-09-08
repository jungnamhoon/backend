package com.hkorea.skyisthelimit.controller.docs;

import com.hkorea.skyisthelimit.common.response.ApiResponse;
import com.hkorea.skyisthelimit.dto.member.request.MemberUpdateRequest;
import com.hkorea.skyisthelimit.dto.member.response.MemberInfoResponse;
import com.hkorea.skyisthelimit.dto.member.response.MemberUpdateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Member", description = "회원 관련 API - JWT 토큰 필요")
public interface MemberControllerDocs {

  @Operation(
      summary = "내 정보 조회",
      description = "로그인한 사용자의 정보를 조회합니다."
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200",
          description = "사용자 정보 불러오기 성공"
      )
  })
  ResponseEntity<ApiResponse<MemberInfoResponse>> getMyInfo(@AuthenticationPrincipal Jwt token);

  @Operation(
      summary = "회원 정보 조회",
      description = "username을 통해 특정 사용자의 정보를 조회합니다."
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200",
          description = "사용자 정보 불러오기 성공"
      )
  })
  ResponseEntity<ApiResponse<MemberInfoResponse>> getUserInfo(@PathVariable String username);

  @Operation(
      summary = "내 정보 수정",
      description = "로그인한 사용자의 프로필 정보를 수정합니다."
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200",
          description = "사용자 정보 수정 성공"
      )
  })
  ResponseEntity<ApiResponse<MemberUpdateResponse>> updateMe(Jwt token,
      @RequestBody MemberUpdateRequest requestDTO);

}
