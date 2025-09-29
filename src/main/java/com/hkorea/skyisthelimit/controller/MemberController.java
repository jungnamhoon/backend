package com.hkorea.skyisthelimit.controller;

import com.hkorea.skyisthelimit.common.response.ApiResponse;
import com.hkorea.skyisthelimit.common.response.SuccessCode;
import com.hkorea.skyisthelimit.common.security.CustomOAuth2User;
import com.hkorea.skyisthelimit.controller.docs.MemberControllerDocs;
import com.hkorea.skyisthelimit.dto.member.request.MemberUpdateRequest;
import com.hkorea.skyisthelimit.dto.member.response.MemberInfoResponse;
import com.hkorea.skyisthelimit.dto.member.response.MemberUpdateResponse;
import com.hkorea.skyisthelimit.dto.member.response.ProfileUpdateResponse;
import com.hkorea.skyisthelimit.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RestController
@RequestMapping("api/members")
@RequiredArgsConstructor
public class MemberController implements MemberControllerDocs {

  private final MemberService memberService;

  @GetMapping("/me")
  public ResponseEntity<ApiResponse<MemberInfoResponse>> getMyInfo(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

    return ApiResponse.of(SuccessCode.OK,
        memberService.getMemberInfo(customOAuth2User.getUsername()));
  }

  @PatchMapping("/me")
  public ResponseEntity<ApiResponse<MemberUpdateResponse>> updateMe(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
      @RequestBody MemberUpdateRequest requestDTO) {

    return ApiResponse.of(SuccessCode.OK,
        memberService.updateMember(customOAuth2User.getUsername(), requestDTO));
  }

  @PutMapping(value = "/me/profile-image", consumes = {"multipart/form-data"})
  public ResponseEntity<ApiResponse<ProfileUpdateResponse>> updateProfileImage(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
      @RequestPart("file") MultipartFile profileImage) throws Exception {

    ProfileUpdateResponse responseDTO = memberService.updateProfileImage(
        customOAuth2User.getUsername(),
        profileImage
    );

    return ApiResponse.of(SuccessCode.OK, responseDTO);
  }


}
