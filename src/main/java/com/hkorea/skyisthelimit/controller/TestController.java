package com.hkorea.skyisthelimit.controller;

import com.hkorea.skyisthelimit.common.response.ApiResponse;
import com.hkorea.skyisthelimit.common.response.SuccessCode;
import com.hkorea.skyisthelimit.common.utils.JwtHelper;
import com.hkorea.skyisthelimit.controller.docs.TestControllerDocs;
import com.hkorea.skyisthelimit.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/test")
@Profile({"local", "dev"})
public class TestController implements TestControllerDocs {

  private final JwtHelper jwtHelper;
  private final StudyService studyService;

  @GetMapping("/public")
  public ResponseEntity<ApiResponse<String>> publicTest() {
    return ApiResponse.of(SuccessCode.OK, "public");
  }

  @GetMapping("/private")
  public ResponseEntity<ApiResponse<String>> privateTest() {
    return ApiResponse.of(SuccessCode.OK, "private");
  }

  @GetMapping("/token")
  public ResponseEntity<ApiResponse<String>> generateTestToken(@RequestParam String username) {
    String accessToken = jwtHelper.createAccessToken(username, username + "@" + "gmail.com", null,
        "ROLE_USER", false);

    return ApiResponse.of(SuccessCode.OK, accessToken);
  }

  @PatchMapping("/problemSetterIdx")
  public ResponseEntity<ApiResponse<String>> updateProblemSetIdx() {
    studyService.rotateProblemSetterIdx();
    return ApiResponse.of(SuccessCode.OK, "rotate ProblemSetterIdx successfully");
  }

}
