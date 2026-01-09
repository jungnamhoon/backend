package com.hkorea.skyisthelimit.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingOptions;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class OpenAiService {

  private final OpenAiChatModel openAiChatModel;
  private final OpenAiEmbeddingModel openAiEmbeddingModel;

  public String generate(Prompt prompt){

    ChatResponse response = openAiChatModel.call(prompt);
    return response.getResult().getOutput().getText();
  }

  public Flux<String> generateStream(String text){
    SystemMessage systemMessage = new SystemMessage("");
    UserMessage userMessage = new UserMessage(text);
    AssistantMessage assistantMessage = new AssistantMessage("");

    OpenAiChatOptions options = OpenAiChatOptions.builder()
        .model("gpt-4.1.-mini")
        .temperature(0.7)
        .build();

    Prompt prompt = new Prompt(List.of(systemMessage,userMessage,assistantMessage),options);

    return openAiChatModel.stream(prompt)
        .mapNotNull(response -> response.getResult().getOutput().getText());
  }

  public List<float[]> generateEmbedding(List<String> texts, String model){

    EmbeddingOptions embeddingOptions = OpenAiEmbeddingOptions.builder()
        .model(model).build();

    EmbeddingRequest prompt = new EmbeddingRequest(texts, embeddingOptions);

    EmbeddingResponse response = openAiEmbeddingModel.call(prompt);
    return response.getResults().stream()
        .map(Embedding::getOutput)
        .toList();
  }

}