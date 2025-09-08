package com.hkorea.skyisthelimit.repository;

import com.hkorea.skyisthelimit.entity.MemberStudy;
import com.hkorea.skyisthelimit.entity.Study;
import com.hkorea.skyisthelimit.entity.StudyProblem;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRepository extends JpaRepository<Study, Integer> {

  @Query(value = """
      SELECT sp.*
      FROM study_problem sp
      JOIN study s ON sp.study_id = s.id
      JOIN member_study ms ON s.id = ms.study_id
      LEFT JOIN member_problem mp
         ON mp.member_id = ms.member_id
        AND mp.problem_id = sp.problem_id
        AND mp.status IN (0,1,2)
      WHERE s.id = :studyId
        AND sp.status = 'UNSOLVED'
      GROUP BY sp.id
      HAVING COUNT(DISTINCT ms.member_id) = COUNT(DISTINCT mp.member_id)
      """, nativeQuery = true)
  List<StudyProblem> findStudyProblemListSolvedByAll(@Param("studyId") Integer studyId);


  @Query(value = """
      SELECT ms.*
      FROM member_study ms
      JOIN study s ON ms.study_id = s.id
      LEFT JOIN member_problem mp
          ON mp.member_id = ms.member_id
         AND mp.problem_id IN (
             SELECT dp.problem_id
             FROM study_daily_problems dp
             WHERE dp.study_id = :studyId
         )
         AND mp.status IN (0,1)  -- 0=SOLVED, 1=MULTI_TRY
      WHERE ms.status = 'APPROVED'
        AND ms.study_id = :studyId
      GROUP BY ms.id
      HAVING COUNT(DISTINCT mp.problem_id) < (
          SELECT COUNT(*)
          FROM study_daily_problems dp
          WHERE dp.study_id = :studyId
      )
      """, nativeQuery = true)
  Set<MemberStudy> findMemberStudiesNotSolvingDailyProblems(@Param("studyId") Integer studyId);
}
