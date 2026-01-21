package com.hkorea.skyisthelimit.analysis.service;

import com.hkorea.skyisthelimit.analysis.entity.Weakness;
import com.hkorea.skyisthelimit.analysis.entity.WrongReason;
import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Component;

@Component
public class WrongReasonSelector {

  private final Random random = new Random();

  public WrongReason selectRandomWrongReason(Weakness weakness) {

    if(weakness == null) {
      return null;
    }

    List<WrongReason>  wrongReasons = weakness.getWrongReasons();
    int randomIndex = random.nextInt(wrongReasons.size());
    return wrongReasons.get(randomIndex);
  }
}
