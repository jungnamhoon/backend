package com.hkorea.skyisthelimit.common.config;

import com.hkorea.skyisthelimit.analysis.entity.Weakness;
import com.hkorea.skyisthelimit.analysis.repository.WeaknessRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements ApplicationRunner {

  private final WeaknessRepository weaknessRepository;

  @Override
  @Transactional
  public void run(ApplicationArguments args) throws Exception {

    log.info("[INIT] Static Weakness 초기화 시작...");

    saveStaticWeakness(1L, "출력 형식 에러");
    saveStaticWeakness(2L, "시간 초과");
    saveStaticWeakness(3L, "메모리 초과");
    saveStaticWeakness(4L, "런타임 에러");
    saveStaticWeakness(5L, "컴파일 에러");

    log.info("[INIT] Static Weakness 데이터 생성 완료.");
  }

  private void saveStaticWeakness(Long id, String summary){

    if(weaknessRepository.existsById(id)){
      return;
    }

    Weakness weakness = new Weakness(summary, new float[1536]);
    weaknessRepository.save(weakness);
  }

}
