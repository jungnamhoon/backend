package com.hkorea.skyisthelimit.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
public class EmitterRepository {

  private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

  public SseEmitter getEmitter(String username) {
    return emitters.get(username);
  }

  public SseEmitter save(String username, SseEmitter emitter) {
    emitters.put(username, emitter);
    return emitter;
  }

  public void deleteByUsername(String username) {
    emitters.remove(username);
  }
}
