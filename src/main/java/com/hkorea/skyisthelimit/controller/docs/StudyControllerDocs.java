package com.hkorea.skyisthelimit.controller.docs;

import com.hkorea.skyisthelimit.common.response.ApiResponse;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Study", description = "스터디 관련 API - JWT 토큰 필요")
public interface StudyControllerDocs {

  @Operation(
      summary = "스터디 목록 조회",
      description = "조건(레벨 범위, 상태, 검색어 등)에 맞는 스터디 목록을 페이징 조회합니다."
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200", description = "스터디 목록 조회 성공"
      )
  })
  ResponseEntity<ApiResponse<Page<StudySummaryResponse>>> getStudyPage(
      @ModelAttribute StudyCriteria criteria
  );

  @Operation(
      summary = "스터디 생성",
      description = "새로운 스터디를 생성합니다. 생성자는 자동으로 스터디장이 됩니다."
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200", description = "스터디 생성 성공"
      )
  })
  ResponseEntity<ApiResponse<StudyCreateResponse>> createStudy(
      @AuthenticationPrincipal Jwt token,
      @RequestBody StudyCreateRequest requestDTO
  );


  @Operation(
      summary = "스터디 조회",
      description = "특정 스터디의 상세 정보를 조회합니다."
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200", description = "스터디 조회 성공"
      )
  })
  ResponseEntity<ApiResponse<StudyInfoResponse>> getStudyInfo(
      @PathVariable Integer studyId
  );

  @Operation(
      summary = "스터디 수정",
      description = "스터디 정보를 수정합니다. 스터디장만 수정할 수 있습니다."
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200", description = "스터디 수정 성공"
      )
  })
  ResponseEntity<ApiResponse<StudyUpdateResponse>> updateStudy(
      @PathVariable Integer studyId,
      @AuthenticationPrincipal Jwt token,
      @RequestBody StudyUpdateRequest requestDTO
  );

  @Operation(
      summary = "스터디 썸네일 이미지 수정",
      description = "특정 스터디의 썸네일 이미지를 업로드 및 수정합니다. " +
          "Multipart/form-data 형식으로 파일을 전송해야 합니다."
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200",
          description = "썸네일 이미지 수정 성공"
      )
  })
  ResponseEntity<ApiResponse<ThumbnailUpdateResponse>> updateThumbnail(
      @PathVariable Integer studyId,
      @AuthenticationPrincipal Jwt token,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "업로드할 썸네일 이미지 (jpg, png 등)",
          required = true
      )
      @RequestPart("file") MultipartFile thumbnailImage
  ) throws Exception;

  @Operation(
      summary = "스터디 참가 요청",
      description = "사용자가 특정 스터디에 참가를 요청합니다."
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200", description = "참가 요청 성공"
      )
  })
  ResponseEntity<ApiResponse<MemberStudyParticipationResponse>> requestJoinStudy(
      @PathVariable Integer studyId,
      @AuthenticationPrincipal Jwt token

  );

  @Operation(
      summary = "스터디 참가 요청 처리",
      description = "스터디장이 참가 요청을 수락하거나 거절합니다."
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200", description = "참가 요청 처리 성공"
      )
  })
  ResponseEntity<ApiResponse<MemberStudyParticipationResponse>> handleRequest(
      @PathVariable Integer studyId,
      @AuthenticationPrincipal Jwt token,
      @PathVariable String username,
      @RequestBody MemberStudyParticipationRequest requestDTO
  );

  @Operation(
      summary = "스터디 초대",
      description = "스터디장이 특정 유저를 스터디에 초대합니다."
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200", description = "스터디 초대 성공"
      )
  })
  ResponseEntity<ApiResponse<MemberStudyParticipationResponse>> inviteToStudy(
      @PathVariable Integer studyId,
      @PathVariable String inviteeUsername,
      @AuthenticationPrincipal Jwt token
  );

  @Operation(
      summary = "스터디 초대 처리",
      description = "초대받은 사용자가 초대를 수락하거나 거절합니다."
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200", description = "초대 처리 성공"
      )
  })
  ResponseEntity<ApiResponse<MemberStudyParticipationResponse>> handleInvitation(
      @PathVariable Integer studyId,
      @RequestBody MemberStudyParticipationRequest requestDTO,
      @AuthenticationPrincipal Jwt token
  );

  @Operation(
      summary = "데일리 문제 생성",
      description = "스터디장이 해당 스터디에 데일리 문제를 등록합니다."
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200", description = "데일리 문제 생성 성공"
      )
  })
  ResponseEntity<ApiResponse<Set<DailyProblemCreateResponse>>> createDailyProblems(
      @PathVariable Integer studyId,
      @AuthenticationPrincipal Jwt token,
      @RequestBody DailyProblemsCreateRequest requestDTO
  );
}
