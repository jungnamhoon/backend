package com.hkorea.skyisthelimit.controller;

import com.hkorea.skyisthelimit.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/problems")
@RequiredArgsConstructor
public class ProblemController {

  private final ProblemService problemService;

}
