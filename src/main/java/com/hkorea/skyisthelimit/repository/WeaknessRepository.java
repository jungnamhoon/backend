package com.hkorea.skyisthelimit.repository;

import com.hkorea.skyisthelimit.entity.MemberProblem;
import com.hkorea.skyisthelimit.entity.Weakness;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WeaknessRepository extends JpaRepository<Weakness, Long> {

  @Query(value = """
        SELECT *, (embedding <=> CAST(:embedding AS vector)) as distance
        FROM weakness
        ORDER BY distance ASC
        LIMIT 1
        """, nativeQuery = true)
  Optional<WeaknessWithDistance> findNearestWeakness(@Param("embedding") float[] embedding);

  @Query(value = """
          SELECT w
          FROM WrongReason wr
          JOIN wr.weakness w
          WHERE wr.memberProblem IN :selectedProblems
          GROUP BY w
          ORDER BY COUNT(wr) DESC
          LIMIT 5
          """)
  List<Weakness> findTopFiveWeaknesses(@Param("selectedProblems") List<MemberProblem> selectedProblems);

  interface WeaknessWithDistance {
    Long getId();
    String getWeaknessSummary();
    Integer getFrequency();
    Double getDistance(); // 계산된 distance 값
  }
}
