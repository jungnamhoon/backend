package com.hkorea.skyisthelimit.analysis.util;

import com.hkorea.skyisthelimit.analysis.dto.internal.ProblemRecommendContext;
import com.hkorea.skyisthelimit.analysis.dto.response.AiRecommendationProblem;
import com.hkorea.skyisthelimit.dto.prompt.IncorrectSummaryDTO;
import java.util.List;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.core.ParameterizedTypeReference;

public class PromptUtil {

  private PromptUtil() {}

  public static Prompt createIncorrectSummaryPrompt(IncorrectSummaryDTO incorrectSummaryDTO){
    SystemMessage systemMessage = new SystemMessage("""
            너는 알고리즘 문제 오답 원인을 분석하는 전문가이다.
            출력된 결과는 이후 벡터 유사도 비교에 사용되므로,
            같은 유형의 오답은 항상 유사한 표현으로 수렴해야 한다.
            
            여러 원인이 있더라도, 가장 결정적인 단 하나의 원인만 한 문장으로 출력하라.
            
            [필수 규칙]
            
            1. 반드시 한국어로 작성한다.
            2. 15~20자 이내의 짧은 명사구로 끝낸다.
            3. 문장 구조는 반드시 다음 형식만 허용한다. 세 단어로 구성한다.
              [대상 명사] + [행위 명사] + 오류
            5. 조사, 접속어 설명 표현은 모두 금지
            6. 도메인 제거: 문제에 나온 소재(토마토, 뱀, 사다리, 친구 등)를 절대 언급하지 마라.
            7. 코드의 변수명, 함수명, 구현 세부사항을 직접 언급하지 않음
            8. 해결 방법이나 수정 방향은 제시하지 않음
            9. 문제에서 나온 소재, 예시, 도메인을 사용해서 표현하지 말것 (예: 토마토, 철수)
            10. 답변의 모든 명사를 '데이터 구조적/알고리즘적 추상 용어'로 대체하라.
            11. 동사, 형용사 부사 등은 사용하지 않고 명사만 사용
            
            [출력 예시]
            초기 상태 설정 오류
            DP 점화식 설정 오류
            예외 조건 미처리 오류
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
        .temperature(0.3)
        .build();

    return new Prompt(List.of(systemMessage, userMessage),options);
  }

  public static Prompt createProblemRecommendPrompt(ProblemRecommendContext context){
    BeanOutputConverter<List<AiRecommendationProblem>> converter =
        new BeanOutputConverter<>(new ParameterizedTypeReference<List<AiRecommendationProblem>>() {});
    String systemInstruction = """
        너는 백준 알고리즘 문제 추천 전문가다.
        
        절대 규칙:
        1. 반드시 JSON 형식으로만 응답한다
        2. 설명 문장, 인사, 마크다운을 절대 포함하지 않는다
        3. 세개의 문제를 추천한다
        4. problemNumber는 숫자만 사용한다
        5. reason은 한국어로 작성한다
        6. reason은 200자 이내로 작성한다
        7. 추천 문제는 사용자가 이전 문제를 풀지 못하게 만든
           '원인이나 어려움이 다시 등장할 수 있는 상황'을 포함해야 한다
        8. 원인을 그대로 반복하거나 단순 요약하면 안 된다
        9. reason은 알고리즘을 잘 모르는 사람도 이해할 수 있게,
           쉽고 구체적인 말로 설명해야 한다
        
        {format}
        """;

    SystemMessage systemMessage = new SystemMessage(
        systemInstruction.replace("{format}", converter.getFormat())
    );

    UserMessage userMessage = new UserMessage("""
        다음 정보를 바탕으로 문제를 추천해줘.
        
        [사용자가 풀었던 문제 번호]
        %s
        
        [문제 태그]
        %s
        
        [이 문제를 풀지 못하게 만든 이유]
        %s
        
        위 문제를 해결하는 과정에서
        어려움을 느꼈던 지점이 다시 나타날 수 있는 문제를 세개 추천해줘.
        """.formatted(
                context.baekjoonId(),
                String.join(", ", context.tags()),
                context.wrongReason()
            ));

    OpenAiChatOptions options = OpenAiChatOptions.builder()
        .model("gpt-4.1-mini")
        .temperature(0.3)
        .build();

    return new Prompt(List.of(systemMessage, userMessage),options);
  }

}
