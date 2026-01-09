package com.hkorea.skyisthelimit.common.utils;

import com.hkorea.skyisthelimit.dto.prompt.IncorrectSummaryDTO;
import java.util.List;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;

public class PromptUtil {

  private PromptUtil() {}

  public static Prompt createIncorrectSummaryPrompt(IncorrectSummaryDTO incorrectSummaryDTO){
    SystemMessage systemMessage = new SystemMessage("""
            너는 백준 알고리즘 문제의 '틀렸습니다' 분석하는 AI다.
            
            절대 규칙:
            1. 반드시 한국어로 답변
            2. 문제의 구체적인 소재나 예시는 절대 언급하지 않는다
            3. 코드의 변수명, 함수명, 구현 세부사항을 직접 언급하지 않음
            4. 문제 해결 과정에서의 개념적 실수를 논리적으로 설명한다
            5. 해결 방법이나 수정 방향은 제시하지 않음
            6. 문제에서 나온 소재, 예시, 도메인을 사용해서 표현하지 말것 (예: 토마토, 철수)
            7. 70자 이내로 작성
            8. 출력은 이후 문제 추천 기준으로 사용됨
            9. 답변의 모든 명사를 '데이터 구조적/알고리즘적 추상 용어'로 대체하라.
              - 예: "익지 않은 토마토" -> 미처리 상태값.
            [좋은 예시]
            - 초기 상태 설정 오류
            - 종료 조건 판별 로직 오류
            """);

    String tags = String.join(", ", incorrectSummaryDTO.tagNames());
    UserMessage userMessage = new UserMessage("""
        백준 문제 ID: %s
        문제 설명(구체적인 문제 소재는 출력에 사용하지 마라):
        %s
        
        입력:
        %s
        출력:
        %s

        제출 코드:
        %s
        
        문제 태그:
        %s
        위의 정보를 바탕으로, 오답의 원인을 '개념 설명' 형태로 작성하라.
    """.formatted(
        incorrectSummaryDTO.problemId(),
        incorrectSummaryDTO.description(),
        incorrectSummaryDTO.inputSpec(),
        incorrectSummaryDTO.outputSpec(),
        incorrectSummaryDTO.sourceCode(),
        tags
    ));

    OpenAiChatOptions options = OpenAiChatOptions.builder()
        .model("gpt-4.1-mini")
        .temperature(0.7)
        .build();

    return new Prompt(List.of(systemMessage, userMessage),options);
  }

  public static Prompt createProblemRecommendPrompt(){
    SystemMessage systemMessage = new SystemMessage("");
    UserMessage userMessage = new UserMessage("");
    AssistantMessage assistantMessage = new AssistantMessage("");
    return new Prompt(systemMessage, userMessage, assistantMessage);
  }
}
