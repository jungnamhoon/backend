package com.hkorea.skyisthelimit.repository;

import com.hkorea.skyisthelimit.entity.Member;
import com.hkorea.skyisthelimit.entity.MemberStudy;
import com.hkorea.skyisthelimit.entity.Study;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberStudyRepository extends JpaRepository<MemberStudy, Integer> {

  Optional<MemberStudy> findByMemberAndStudy(Member member, Study study);

  List<MemberStudy> findByMember(Member member);
}
