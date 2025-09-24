package com.hkorea.skyisthelimit.controller;

import com.hkorea.skyisthelimit.common.response.ApiResponse;
import com.hkorea.skyisthelimit.common.response.SuccessCode;
import com.hkorea.skyisthelimit.controller.docs.MemberControllerDocs;
import com.hkorea.skyisthelimit.dto.member.request.MemberUpdateRequest;
import com.hkorea.skyisthelimit.dto.member.response.MemberInfoResponse;
import com.hkorea.skyisthelimit.dto.member.response.MemberUpdateResponse;
import com.hkorea.skyisthelimit.dto.member.response.ProfileUpdateResponse;
import com.hkorea.skyisthelimit.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("api/members")
@RequiredArgsConstructor
public class MemberController implements MemberControllerDocs {

  private final MemberService memberService;

  @GetMapping("/me")
  public ResponseEntity<ApiResponse<MemberInfoResponse>> getMyInfo(
      @AuthenticationPrincipal Jwt token) {
    return ApiResponse.of(SuccessCode.OK, memberService.getMemberInfo(token.getClaim("username")));
  }

  @GetMapping("/{username}")
  public ResponseEntity<ApiResponse<MemberInfoResponse>> getUserInfo(
      @PathVariable String username) {
    return ApiResponse.of(SuccessCode.OK, memberService.getMemberInfo(username));
  }

  @PatchMapping("/me")
  public ResponseEntity<ApiResponse<MemberUpdateResponse>> updateMe(
      @AuthenticationPrincipal Jwt token,
      @RequestBody MemberUpdateRequest requestDTO) {

    return ApiResponse.of(SuccessCode.OK,
        memberService.updateMember(token.getClaim("username"), requestDTO));
  }

  @PostMapping(value = "/me/profile-image", consumes = {"multipart/form-data"})
  public ResponseEntity<ApiResponse<ProfileUpdateResponse>> uploadProfileImage(
      @AuthenticationPrincipal Jwt token,
      @RequestPart("file") MultipartFile profileImage) throws Exception {

    ProfileUpdateResponse responseDTO = memberService.updateProfileImage(
        token.getClaim("username"),
        profileImage
    );

    return ApiResponse.of(SuccessCode.OK, responseDTO);
  }


}
