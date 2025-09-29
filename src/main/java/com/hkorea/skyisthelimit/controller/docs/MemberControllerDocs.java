package com.hkorea.skyisthelimit.controller.docs;

import com.hkorea.skyisthelimit.common.response.ApiResponse;
import com.hkorea.skyisthelimit.common.security.CustomOAuth2User;
import com.hkorea.skyisthelimit.dto.member.request.MemberUpdateRequest;
import com.hkorea.skyisthelimit.dto.member.response.MemberInfoResponse;
import com.hkorea.skyisthelimit.dto.member.response.MemberUpdateResponse;
import com.hkorea.skyisthelimit.dto.member.response.ProfileUpdateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

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
  ResponseEntity<ApiResponse<MemberInfoResponse>> getMyInfo(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User);

//  @Operation(
//      summary = "회원 정보 조회",
//      description = "username을 통해 특정 사용자의 정보를 조회합니다."
//  )
//  @ApiResponses({
//      @io.swagger.v3.oas.annotations.responses.ApiResponse(
//          responseCode = "200",
//          description = "사용자 정보 불러오기 성공"
//      )
//  })
//  ResponseEntity<ApiResponse<MemberInfoResponse>> getUserInfo(@PathVariable String username);

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
  ResponseEntity<ApiResponse<MemberUpdateResponse>> updateMe(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
      @RequestBody MemberUpdateRequest requestDTO);


  @Operation(
      summary = "프로필 이미지 변경",
      description = "로그인한 사용자의 프로필 이미지를 업로드 및 변경합니다. " +
          "Multipart/form-data 형식으로 파일을 전송해야 합니다."
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200",
          description = "프로필 이미지 수정 성공"
      )
  })
  ResponseEntity<ApiResponse<ProfileUpdateResponse>> updateProfileImage(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "업로드할 프로필 이미지 (jpg, png 등)",
          required = true
      )
      @RequestPart("file") MultipartFile profileImage
  ) throws Exception;

}
