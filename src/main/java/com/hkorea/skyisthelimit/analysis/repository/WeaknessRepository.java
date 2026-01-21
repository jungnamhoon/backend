package com.hkorea.skyisthelimit.analysis.repository;

import com.hkorea.skyisthelimit.entity.MemberProblem;
import com.hkorea.skyisthelimit.analysis.entity.Weakness;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WeaknessRepository extends JpaRepository<Weakness, Long>, WeaknessRepositoryCustom{

  @Query(value = """
      SELECT *, (embedding <=> CAST(:embedding AS vector)) as distance
      FROM weakness
      WHERE id > 5  -- 시스템 공통 데이터(1~5)는 검색 대상에서 제외
      AND embedding IS NOT NULL -- 안전장치
      ORDER BY distance ASC
      LIMIT 1
      """, nativeQuery = true)
  Optional<WeaknessWithDistance> findNearestWeakness(@Param("embedding") float[] embedding);

  interface WeaknessWithDistance {
    Long getId();
    String getWeaknessSummary();
    Integer getFrequency();
    Double getDistance(); // 계산된 distance 값
  }
}
