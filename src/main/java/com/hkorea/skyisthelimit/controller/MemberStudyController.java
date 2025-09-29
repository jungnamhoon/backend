package com.hkorea.skyisthelimit.controller;

import com.hkorea.skyisthelimit.common.response.ApiResponse;
import com.hkorea.skyisthelimit.common.response.SuccessCode;
import com.hkorea.skyisthelimit.common.security.CustomOAuth2User;
import com.hkorea.skyisthelimit.controller.docs.MemberStudyControllerDocs;
import com.hkorea.skyisthelimit.dto.memberstudy.response.MemberStudyResponse;
import com.hkorea.skyisthelimit.service.MemberStudyService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberStudyController implements MemberStudyControllerDocs {

  private final MemberStudyService memberStudyService;


  @GetMapping("/me/studies")
  public ResponseEntity<ApiResponse<List<MemberStudyResponse>>> getMyStudyList(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

    return ApiResponse.of(SuccessCode.OK,
        memberStudyService.getMemberStudyList(customOAuth2User.getUsername()));
  }
}
