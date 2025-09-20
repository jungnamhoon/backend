package com.hkorea.skyisthelimit.controller;

import com.hkorea.skyisthelimit.common.response.ApiResponse;
import com.hkorea.skyisthelimit.common.response.SuccessCode;
import com.hkorea.skyisthelimit.controller.docs.MemberProblemControllerDocs;
import com.hkorea.skyisthelimit.dto.criteria.MemberProblemCriteria;
import com.hkorea.skyisthelimit.dto.criteria.RandomProblemCriteria;
import com.hkorea.skyisthelimit.dto.memberproblem.request.MemberProblemTagCountResponse;
import com.hkorea.skyisthelimit.dto.memberproblem.request.NoteRequestDTO;
import com.hkorea.skyisthelimit.dto.memberproblem.request.SolveRequest;
import com.hkorea.skyisthelimit.dto.memberproblem.request.SolveResponse;
import com.hkorea.skyisthelimit.dto.memberproblem.response.MemberProblemResponse;
import com.hkorea.skyisthelimit.dto.memberproblem.response.NoteResponse;
import com.hkorea.skyisthelimit.dto.memberproblem.response.RandomProblemResponse;
import com.hkorea.skyisthelimit.service.MemberProblemService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberProblemController implements MemberProblemControllerDocs {

  private final MemberProblemService memberProblemService;

  @GetMapping("/{username}/problems")
  public ResponseEntity<ApiResponse<Page<MemberProblemResponse>>> getMemberProblemPage(
      @PathVariable String username, @ModelAttribute MemberProblemCriteria criteria) {

    Page<MemberProblemResponse> responsePage = memberProblemService.getMemberProblemPage(
        username,
        criteria);
    return ApiResponse.of(SuccessCode.OK, responsePage);
  }

  @PostMapping("/{username}/problems")
  public ResponseEntity<ApiResponse<SolveResponse>> solveProblem(
      @PathVariable String username, @RequestBody SolveRequest requestDTO) {

    SolveResponse responseDTO = memberProblemService.solveProblem(
        username,
        requestDTO.getSubmitId(),
        requestDTO.getBaekjoonId(),
        requestDTO.getIsSolved());
    return ApiResponse.of(SuccessCode.OK, responseDTO);
  }

  @PostMapping("/me/problems")
  public ResponseEntity<ApiResponse<SolveResponse>> solveProblem(
      @AuthenticationPrincipal Jwt token,
      @RequestBody SolveRequest requestDTO) {

    SolveResponse responseDTO = memberProblemService.solveProblem(
        token.getClaim("username") != null ? token.getClaim("username") : token.getClaim("sub"),
        requestDTO.getSubmitId(),
        requestDTO.getBaekjoonId(),
        requestDTO.getIsSolved());
    return ApiResponse.of(SuccessCode.OK, responseDTO);
  }

  @GetMapping("/{username}/problems/{baekjoonId}/note")
  public ResponseEntity<ApiResponse<NoteResponse>> getNote(@PathVariable String username,
      @PathVariable Integer baekjoonId) {

    NoteResponse responseDTO = memberProblemService.getNote(username, baekjoonId);
    return ApiResponse.of(SuccessCode.OK, responseDTO);
  }

  @PostMapping("/{username}/problems/{baekjoonId}/note")
  public ResponseEntity<ApiResponse<NoteResponse>> writeNote(@PathVariable String username,
      @PathVariable Integer baekjoonId, @RequestBody NoteRequestDTO requestDTO) {

    NoteResponse responseDTO = memberProblemService.writeNote(username, baekjoonId,
        requestDTO.getContent());
    return ApiResponse.of(SuccessCode.OK, responseDTO);
  }

  @GetMapping("/{username}/random-problem")
  public ResponseEntity<ApiResponse<RandomProblemResponse>> getRandomProblem(
      @PathVariable String username, @ModelAttribute RandomProblemCriteria criteria) {

    RandomProblemResponse responseDTO = memberProblemService.getRandomProblem(username,
        criteria);
    return ApiResponse.of(SuccessCode.OK, responseDTO);
  }

  @GetMapping("/{username}/statistics")
  public ResponseEntity<ApiResponse<List<MemberProblemTagCountResponse>>> getMemberStatistics(
      @PathVariable String username) {
    List<MemberProblemTagCountResponse> responseDTOList = memberProblemService.getMemberStatistics(
        username);
    return ApiResponse.of(SuccessCode.OK, responseDTOList);
  }
  
}
