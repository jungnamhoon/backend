package com.hkorea.skyisthelimit.repository;

import com.hkorea.skyisthelimit.entity.Study;
import com.hkorea.skyisthelimit.entity.StudyProblem;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyProblemRepository extends JpaRepository<StudyProblem, Integer> {

  List<StudyProblem> findByStudyId(Integer studyId);

  List<StudyProblem> findByStudyAndAssignedDate(Study study, LocalDate assignedDate);

}
