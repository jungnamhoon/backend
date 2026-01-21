package com.hkorea.skyisthelimit.common.infrastructure.ai;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class OpenAiClientTest {

  @Mock
  private OpenAiChatModel chatModel;

  @Mock
  private OpenAiEmbeddingModel embeddingModel;

  @InjectMocks
  private OpenAiClient openAiClient;

  @Nested
  @DisplayName("generateText 테스트")
  class GenerateText {

    @Test
    @DisplayName("성공: OpenAI가 정상 응답을 주면 텍스트를 반환한다")
    void success() {
      // given
      String expectedText = "반가워요!";
      ChatResponse mockResponse = mock(ChatResponse.class);
      Generation mockGeneration = mock(Generation.class);
      AssistantMessage assistantMessage = new AssistantMessage(expectedText);

      given(chatModel.call(any(Prompt.class))).willReturn(mockResponse);
      given(mockResponse.getResult()).willReturn(mockGeneration);
      given(mockGeneration.getOutput()).willReturn(assistantMessage);

      // when
      String result = openAiClient.generateText(new Prompt("안녕"));

      // then
      assertThat(result).isEqualTo(expectedText);
    }

    @Test
    @DisplayName("실패: 어떤 에러라도 발생하면 RuntimeException으로 감싸서 던진다")
    void throwsRuntimeException() {
      // given
      given(chatModel.call(any(Prompt.class))).willThrow(new RuntimeException("API Error"));

      // when & then
      assertThatThrownBy(() -> openAiClient.generateText(new Prompt("안녕")))
          .isInstanceOf(RuntimeException.class)
          .hasMessageContaining("AI 서비스 이용이 일시적으로 불가능합니다.");
    }
  }

  @Nested
  @DisplayName("Embedding 테스트")
  class EmbeddingTest {

    @Test
    @DisplayName("성공: 임베딩 리스트를 반환한다")
    void generateEmbeddingsSuccess() {
      // given
      EmbeddingResponse mockResponse = mock(EmbeddingResponse.class);
      Embedding mockEmb1 = mock(Embedding.class);
      float[] vector = {0.1f, 0.2f};

      given(embeddingModel.call(any(EmbeddingRequest.class))).willReturn(mockResponse);
      given(mockResponse.getResults()).willReturn(List.of(mockEmb1));
      given(mockEmb1.getOutput()).willReturn(vector);

      // when
      List<float[]> results = openAiClient.generateEmbeddings(new EmbeddingRequest(List.of("test"), null));

      // then
      assertThat(results).hasSize(1);
      assertThat(results.get(0)).isEqualTo(vector);
    }

    @Test
    @DisplayName("성공: 단건 임베딩 요청 시 첫 번째 요소를 반환한다")
    void generateEmbeddingSuccess() {
      // given
      EmbeddingResponse mockResponse = mock(EmbeddingResponse.class);
      Embedding mockEmb1 = mock(Embedding.class);
      float[] vector = {0.5f, 0.6f};

      given(embeddingModel.call(any(EmbeddingRequest.class))).willReturn(mockResponse);
      given(mockResponse.getResults()).willReturn(List.of(mockEmb1));
      given(mockEmb1.getOutput()).willReturn(vector);

      // when
      float[] result = openAiClient.generateEmbedding(new EmbeddingRequest(List.of("test"), null));

      // then
      assertThat(result).isEqualTo(vector);
    }

    @Test
    @DisplayName("실패: 결과가 비어있으면 RuntimeException을 던진다")
    void generateEmbeddingEmptyThrowsException() {
      // given
      EmbeddingResponse mockResponse = mock(EmbeddingResponse.class);
      given(embeddingModel.call(any(EmbeddingRequest.class))).willReturn(mockResponse);
      given(mockResponse.getResults()).willReturn(List.of());

      // when & then
      assertThatThrownBy(() -> openAiClient.generateEmbedding(new EmbeddingRequest(List.of("test"), null)))
          .isInstanceOf(RuntimeException.class)
          .hasMessageContaining("AI 서비스로부터 유효한 임베딩 결과를 받지 못했습니다");
    }
  }
}