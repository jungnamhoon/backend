package com.hkorea.skyisthelimit.dto.memberstudy.request;

import com.hkorea.skyisthelimit.entity.enums.MemberStudyStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MemberStudyParticipationRequest {

  @Schema(
      description = "스터디 참가 요청 상태",
      example = "APPROVED",
      allowableValues = {"PENDING", "APPROVED", "REJECTED"}
  )
  private MemberStudyStatus requestStatus;

}
