package com.hkorea.skyisthelimit.controller;

import com.hkorea.skyisthelimit.common.response.ApiResponse;
import com.hkorea.skyisthelimit.common.response.SuccessCode;
import com.hkorea.skyisthelimit.controller.docs.StudyControllerDocs;
import com.hkorea.skyisthelimit.dto.criteria.StudyCriteria;
import com.hkorea.skyisthelimit.dto.memberstudy.request.MemberStudyParticipationRequest;
import com.hkorea.skyisthelimit.dto.memberstudy.response.MemberStudyParticipationResponse;
import com.hkorea.skyisthelimit.dto.problem.request.DailyProblemsCreateRequest;
import com.hkorea.skyisthelimit.dto.problem.response.DailyProblemCreateResponse;
import com.hkorea.skyisthelimit.dto.study.request.StudyCreateRequest;
import com.hkorea.skyisthelimit.dto.study.request.StudyUpdateRequest;
import com.hkorea.skyisthelimit.dto.study.response.StudyCreateResponse;
import com.hkorea.skyisthelimit.dto.study.response.StudyInfoResponse;
import com.hkorea.skyisthelimit.dto.study.response.StudySummaryResponse;
import com.hkorea.skyisthelimit.dto.study.response.StudyUpdateResponse;
import com.hkorea.skyisthelimit.dto.study.response.ThumbnailUpdateResponse;
import com.hkorea.skyisthelimit.service.MemberStudyService;
import com.hkorea.skyisthelimit.service.StudyService;
import jakarta.validation.Valid;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/studies")
@RequiredArgsConstructor
public class StudyController implements StudyControllerDocs {

  private final StudyService studyService;
  private final MemberStudyService memberStudyService;

  @GetMapping
  public ResponseEntity<ApiResponse<Page<StudySummaryResponse>>> getStudyPage(
      @ModelAttribute StudyCriteria criteria) {

    Page<StudySummaryResponse> responsePage = studyService.getStudyPage(criteria);
    return ApiResponse.of(SuccessCode.OK, responsePage);
  }

  @PostMapping
  public ResponseEntity<ApiResponse<StudyCreateResponse>> createStudy(
      @AuthenticationPrincipal Jwt token, @Valid @RequestBody StudyCreateRequest requestDTO)
      throws Exception {

    StudyCreateResponse responseDTO = studyService.createStudy(token.getClaim("username"),
        requestDTO);
    return ApiResponse.of(SuccessCode.OK, responseDTO);
  }

  @GetMapping("/{studyId}")
  public ResponseEntity<ApiResponse<StudyInfoResponse>> getStudyInfo(
      @PathVariable Integer studyId) {

    return ApiResponse.of(SuccessCode.OK, studyService.getStudyInfo(studyId));
  }

  @PatchMapping("/{studyId}")
  public ResponseEntity<ApiResponse<StudyUpdateResponse>> updateStudy(
      @PathVariable Integer studyId, @AuthenticationPrincipal Jwt token,
      @RequestBody StudyUpdateRequest requestDTO
  ) {

    StudyUpdateResponse responseDTO = studyService.updateStudy(studyId, token.getClaim("username"),
        requestDTO);

    return ApiResponse.of(SuccessCode.OK, responseDTO);
  }

  @PutMapping(value = "/{studyId}/thumbnail-image", consumes = {"multipart/form-data"})
  public ResponseEntity<ApiResponse<ThumbnailUpdateResponse>> updateThumbnail(
      @PathVariable Integer studyId,
      @AuthenticationPrincipal Jwt token,
      @RequestPart("file") MultipartFile thumbnailImage) throws Exception {

    ThumbnailUpdateResponse responseDTO = studyService.updateThumbnail(
        studyId,
        token.getClaim("username"),
        thumbnailImage
    );

    return ApiResponse.of(SuccessCode.OK, responseDTO);
  }

  // 나 -> 스터디 참가 요청을 보내는 사람
  @PostMapping("/{studyId}/requests")
  public ResponseEntity<ApiResponse<MemberStudyParticipationResponse>> requestJoinStudy(
      @PathVariable Integer studyId,
      @AuthenticationPrincipal Jwt token) {

    MemberStudyParticipationResponse responseDTO = memberStudyService.requestJoinStudy(
        token.getClaim("username"), studyId);
    return ApiResponse.of(SuccessCode.OK, responseDTO);
  }

  // 나 -> 스터디 참가 요청을 받는 사람
  @PatchMapping("/{studyId}/requests/{requester-username}")
  public ResponseEntity<ApiResponse<MemberStudyParticipationResponse>> handleRequest(
      @PathVariable Integer studyId,
      @AuthenticationPrincipal Jwt token,
      @PathVariable("requester-username") String requesterUsername,
      @RequestBody MemberStudyParticipationRequest requestDTO
  ) {

    MemberStudyParticipationResponse responseDTO = memberStudyService.handleRequest(
        studyId,
        token.getClaim("username"),
        requesterUsername,
        requestDTO.getRequestStatus());
    return ApiResponse.of(SuccessCode.OK, responseDTO);
  }

  // 나 -> 초대 보내는 사람
  @PostMapping("/{studyId}/invites/{invitee-username}")
  public ResponseEntity<ApiResponse<MemberStudyParticipationResponse>> inviteToStudy(
      @PathVariable Integer studyId,
      @PathVariable("invitee-username") String inviteeUsername,
      @AuthenticationPrincipal Jwt token) {

    MemberStudyParticipationResponse responseDTO = memberStudyService.inviteToStudy(
        studyId,
        token.getClaim("username"),
        inviteeUsername
    );

    return ApiResponse.of(SuccessCode.OK, responseDTO);
  }

  // 나 -> 초대 받는 사람
  @PatchMapping("/{studyId}/invites")
  public ResponseEntity<ApiResponse<MemberStudyParticipationResponse>> handleInvitation(
      @PathVariable Integer studyId,
      @RequestBody MemberStudyParticipationRequest requestDTO,
      @AuthenticationPrincipal Jwt token) {

    MemberStudyParticipationResponse responseDTO = memberStudyService.handleInvitation(
        studyId,
        token.getClaim("username"),
        requestDTO.getRequestStatus()
    );

    return ApiResponse.of(SuccessCode.OK, responseDTO);
  }

  @PostMapping("/{studyId}/daily-problems")
  public ResponseEntity<ApiResponse<Set<DailyProblemCreateResponse>>> createDailyProblems(
      @PathVariable Integer studyId,
      @AuthenticationPrincipal Jwt token,
      @Valid @RequestBody DailyProblemsCreateRequest requestDTO) {

    Set<DailyProblemCreateResponse> responseDTOS = studyService.createDailyProblems(studyId,
        token.getClaim("username"), requestDTO);
    return ApiResponse.of(SuccessCode.OK, responseDTOS);
  }

}
