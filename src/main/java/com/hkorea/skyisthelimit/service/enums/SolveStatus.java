package com.hkorea.skyisthelimit.service.enums;

import com.hkorea.skyisthelimit.entity.MemberProblem;
import com.hkorea.skyisthelimit.entity.enums.MemberProblemStatus;

public enum SolveStatus {

  NOTHING,
  CORRECT_NOT_TRIED,
  CORRECT_ALREADY_SOLVED,
  CORRECT_ALREADY_UNSOLVED,
  WRONG_NOT_TRIED,
  WRONG_ALREADY_TRIED,
  ;


  public static SolveStatus of(MemberProblem memberProblem, Long submitId, boolean isSolved) {

    if (memberProblem == null && isSolved) {
      return CORRECT_NOT_TRIED;
    }

    if (memberProblem == null && !isSolved) {
      return WRONG_NOT_TRIED;
    }

    if (memberProblem.getLastSubmitId().equals(submitId)) {
      return NOTHING;
    }

    if (memberProblem != null && isSolved) {

      MemberProblemStatus status = memberProblem.getStatus();
      if (status == MemberProblemStatus.SOLVED || status == MemberProblemStatus.MULTI_TRY) {
        return CORRECT_ALREADY_SOLVED;
      } else if (status == MemberProblemStatus.UNSOLVED) {
        return CORRECT_ALREADY_UNSOLVED;
      }
    }

    if (memberProblem != null && !isSolved) {
      return WRONG_ALREADY_TRIED;
    }

    throw new IllegalStateException("Unknown combination: isSolved=" + isSolved +
        ", memberProblem=" + memberProblem);
  }

}
