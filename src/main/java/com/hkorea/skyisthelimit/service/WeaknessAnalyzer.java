package com.hkorea.skyisthelimit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkorea.skyisthelimit.WeaknessStat;
import com.hkorea.skyisthelimit.entity.WrongReason;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WeaknessAnalyzer {

  private final ObjectMapper objectMapper;

  /**
   * WrongReason 리스트를 클러스터링하여 약점 통계를 생성합니다.
   */
  public List<WeaknessStat> analyze(List<WrongReason> wrongReasons) {
    if (wrongReasons.isEmpty()) {
      return Collections.emptyList();
    }

    // 1. 임베딩 데이터를 DoublePoint로 변환
    List<DoublePoint> points = convertToPoints(wrongReasons);

    // 2. DBSCAN 클러스터링 실행 (eps: 0.15, minPts: 1)
    DBSCANClusterer<DoublePoint> clusterer = new DBSCANClusterer<>(0.15, 1);
    List<Cluster<DoublePoint>> clusters = clusterer.cluster(points);

    // 3. 통계 데이터 추출
    List<WeaknessStat> stats = buildStats(clusters, points, wrongReasons);

    // 4. 빈도수 기준 내림차순 정렬
    stats.sort(Comparator.comparingInt(WeaknessStat::count).reversed());

    return stats;
  }

  private List<DoublePoint> convertToPoints(List<WrongReason> wrongReasons) {
    Random random = new Random();
    List<DoublePoint> points = new ArrayList<>();

    for(WrongReason wrongReason : wrongReasons) {
      try{
        double[] vector = objectMapper.readValue(wrongReason.getEmbeddingJson(), double[].class);
        double epsilon = 1e-8;
        vector[0] += (random.nextDouble()-0.5) * epsilon;
        points.add(new DoublePoint(vector));

      } catch (JsonProcessingException e){
        throw new RuntimeException(e);
      }
    }

    return points;

  }

  private List<WeaknessStat> buildStats(List<Cluster<DoublePoint>> clusters,
      List<DoublePoint> allPoints,
      List<WrongReason> wrongReasons) {

    List<WeaknessStat> stats = new ArrayList<>();
    Set<DoublePoint> clusteredPoints = new HashSet<>();

    if(clusters.isEmpty()){
      System.out.println("No clusters");
    }

    // 클러스터링된 데이터 처리
    for (Cluster<DoublePoint> cluster : clusters) {
      List<DoublePoint> clusterPoints = cluster.getPoints();
      if (clusterPoints.isEmpty()) {
        continue;
      }
      // 클러스터 내의 첫 번째 포인트를 대표값으로 설정
      DoublePoint representativePoint = clusterPoints.get(0);
      int originalIndex = allPoints.indexOf(representativePoint);

      stats.add(new WeaknessStat(wrongReasons.get(originalIndex), clusterPoints.size()));
      clusteredPoints.addAll(clusterPoints);
    }

    // 클러스터에 포함되지 않은 노이즈(Outlier) 처리
    for (int i = 0; i < allPoints.size(); i++) {
      if (!clusteredPoints.contains(allPoints.get(i))) {
        stats.add(new WeaknessStat(wrongReasons.get(i), 1));
      }
    }

    return stats;
  }
}
