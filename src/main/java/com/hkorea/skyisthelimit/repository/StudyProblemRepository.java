package com.hkorea.skyisthelimit.repository;

import com.hkorea.skyisthelimit.entity.StudyProblem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyProblemRepository extends JpaRepository<StudyProblem, Integer> {

  List<StudyProblem> findByStudyId(Integer studyId);
}
