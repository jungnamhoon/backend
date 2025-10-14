package com.hkorea.skyisthelimit.repository;

import com.hkorea.skyisthelimit.entity.MemberStudy;
import com.hkorea.skyisthelimit.entity.Study;
import com.hkorea.skyisthelimit.entity.StudyProblem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRepository extends JpaRepository<Study, Integer> {

  @Query("""
      SELECT DISTINCT sp
      FROM StudyProblem sp
      JOIN FETCH sp.problem p
      WHERE sp.study.id = :studyId
        AND NOT EXISTS (
          SELECT 1
          FROM MemberStudy ms
          WHERE ms.study = sp.study
            AND NOT EXISTS (
              SELECT 1
              FROM MemberProblem mp
              WHERE mp.member = ms.member
                AND mp.problem = sp.problem
                AND mp.status IN (com.hkorea.skyisthelimit.entity.enums.MemberProblemStatus.SOLVED,
                                  com.hkorea.skyisthelimit.entity.enums.MemberProblemStatus.MULTI_TRY)
            )
        )
      """)
  List<StudyProblem> findStudyProblemListSolvedByAll(@Param("studyId") Integer studyId);

  @Query(value = """
          SELECT ms.*
          FROM member_study ms
          JOIN study s ON ms.study_id = s.id
          LEFT JOIN member_problem mp
              ON mp.member_id = ms.member_id
              AND mp.problem_id IN (
                  SELECT sp.problem_id
                  FROM study_problem sp
                  WHERE sp.study_id = :studyId
                    AND sp.assigned_date = CURRENT_DATE
              )
              AND mp.status IN (0, 1)  -- 0=SOLVED, 1=MULTI_TRY
          WHERE ms.status = 'APPROVED'
            AND ms.study_id = :studyId
          GROUP BY ms.id
          HAVING COUNT(DISTINCT mp.problem_id) < (
              SELECT COUNT(*)
              FROM study_problem sp
              WHERE sp.study_id = :studyId
                AND sp.assigned_date = CURRENT_DATE
          )
      """, nativeQuery = true)
  List<MemberStudy> findMemberStudiesNotSolvingDailyProblems(@Param("studyId") Integer studyId);
}
