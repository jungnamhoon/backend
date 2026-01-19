package com.hkorea.skyisthelimit.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "weakness")
public class Weakness {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String weaknessSummary;
  private int frequency;

  @OneToMany(mappedBy = "weakness", cascade = CascadeType.ALL)
  private final List<WrongReason> wrongReasons = new ArrayList<>();

  @Lob
  @Column(columnDefinition = "TEXT")
  private String embeddingJson;

  @JdbcTypeCode(SqlTypes.VECTOR)
  @Column(columnDefinition = "vector(1536)")
  private float[] embedding;

  public void incrementFrequency() {
    frequency++;
  }

  public Weakness(String weaknessSummary,float[] embedding) {
    this.weaknessSummary = weaknessSummary;
    this.frequency = 1;
    this.embedding = embedding;
  }

}
