package com.hkorea.skyisthelimit.controller;

import com.hkorea.skyisthelimit.common.response.ApiResponse;
import com.hkorea.skyisthelimit.common.response.SuccessCode;
import com.hkorea.skyisthelimit.controller.docs.TestControllerDocs;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/test")
public class TestController implements TestControllerDocs {

  @GetMapping("/public")
  public ResponseEntity<ApiResponse<String>> publicTest() {
    return ApiResponse.of(SuccessCode.OK, "public");
  }

  @GetMapping("/private")
  public ResponseEntity<ApiResponse<String>> privateTest() {
    return ApiResponse.of(SuccessCode.OK, "private");
  }
}
