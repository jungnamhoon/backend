package com.hkorea.skyisthelimit.analysis.repository;

import com.hkorea.skyisthelimit.analysis.entity.QWeakness;
import com.hkorea.skyisthelimit.analysis.entity.Weakness;
import com.hkorea.skyisthelimit.dto.criteria.Criteria;
import com.hkorea.skyisthelimit.entity.QProblem;
import java.util.List;

public interface WeaknessRepositoryCustom {
  List<Weakness> findTopWeaknesses(String username, Criteria<QProblem> criteria, int limit);
}
