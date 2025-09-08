package com.hkorea.skyisthelimit.entity;


import com.hkorea.skyisthelimit.entity.enums.MemberStudyStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "member_study")
public class MemberStudy {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "study_id")
  private Study study;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sender_id")
  private Member sender;

  @Enumerated(EnumType.STRING)
  private MemberStudyStatus status;

  public static MemberStudy create(Member member, Study study, MemberStudyStatus status) {
    return MemberStudy.builder()
        .member(member)
        .study(study)
        .status(status)
        .build();
  }
}
