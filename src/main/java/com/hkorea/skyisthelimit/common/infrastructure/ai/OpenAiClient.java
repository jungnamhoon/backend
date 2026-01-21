package com.hkorea.skyisthelimit.common.infrastructure.ai;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenAiClient {

  private final OpenAiChatModel chatModel;
  private final OpenAiEmbeddingModel embeddingModel;

  public String generateText(Prompt prompt) {

    try {
      ChatResponse response = chatModel.call(prompt);
      return response.getResult().getOutput().getText();

    } catch (Exception e) {
      log.error("[AI SYSTEM ERROR] OpenAI 처리 중 에러 발생. Prompt: {}, Message: {}",
          prompt, e.getMessage(), e);
      throw new RuntimeException("AI 서비스 이용이 일시적으로 불가능합니다.", e);
    }
  }

  public List<float[]> generateEmbeddings(EmbeddingRequest request) {
    try {
      EmbeddingResponse response = embeddingModel.call(request);
      return response.getResults().stream()
          .map(Embedding::getOutput)
          .toList();
    } catch (Exception e) {
      log.error("[AI SYSTEM ERROR] 임베딩 생성 중 오류 발생. Request: {}, Message: {}",
          request, e.getMessage(), e);
      throw new RuntimeException("AI 서비스 이용이 일시적으로 불가능합니다.", e);
    }
  }

  public float[] generateEmbedding(EmbeddingRequest request){

    List<float[]> embeddings = generateEmbeddings(request);

    if (embeddings == null || embeddings.isEmpty()) {
      log.warn("[AI DATA WARNING] 임베딩 요청은 성공했으나 반환된 결과가 비어있습니다. Request: {}", request);
      throw new RuntimeException("AI 서비스로부터 유효한 임베딩 결과를 받지 못했습니다.");
    }

    return embeddings.get(0);
  }

}
