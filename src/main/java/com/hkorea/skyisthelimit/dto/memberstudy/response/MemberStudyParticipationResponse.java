package com.hkorea.skyisthelimit.dto.memberstudy.response;

import com.hkorea.skyisthelimit.entity.enums.MemberStudyStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberStudyParticipationResponse {

  @Schema(description = "스터디 ID", example = "1")
  private Integer studyId;

  @Schema(description = "스터디 제목", example = "백준 골드 달성반")
  private String studyTitle;

  @Schema(description = "스터디 방장 사용자명", example = "adminUser")
  private String studyAdminUsername;

  @Schema(description = "스터디 참가자 사용자명", example = "participantUser")
  private String studyParticipantUsername;

  @Schema(description = "참가 요청 상태",
      example = "PENDING",
      allowableValues = {"PENDING", "APPROVED", "REJECTED"})
  private MemberStudyStatus requestStatus;
}
