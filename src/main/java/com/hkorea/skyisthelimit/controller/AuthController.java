package com.hkorea.skyisthelimit.controller;

import com.hkorea.skyisthelimit.controller.docs.AuthControllerDocs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AuthController implements AuthControllerDocs {

  @GetMapping("/oauth2/authorization/google")
  public String loginWithGoogle() {
    return "redirect:/oauth2/authorization/google";
  }
}
