package com.hkorea.skyisthelimit.service;

import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

@Service
public class MockOpenAiService {

  public String generate(Prompt prompt) {
    try {
      Thread.sleep(4000);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    return """
            [
              {
                "recommendedProblemId": 9012,
                "reason": "테스트용 추천 사유입니다. 스택을 활용한 기초 연습에 적합합니다."
              },
              {
                "recommendedProblemId": 10799,
                "reason": "괄호 처리를 응용할 수 있는 중급 문제입니다."
              },
              {
                "recommendedProblemId": 4949,
                "reason": "다양한 예외 케이스를 다루는 연습이 됩니다."
              }
            ]
            """;
  }
}
