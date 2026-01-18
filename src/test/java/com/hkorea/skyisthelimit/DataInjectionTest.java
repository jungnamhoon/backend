package com.hkorea.skyisthelimit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkorea.skyisthelimit.entity.MemberProblem;
import com.hkorea.skyisthelimit.entity.WrongReason;
import com.hkorea.skyisthelimit.repository.MemberProblemRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
public class DataInjectionTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MemberProblemRepository memberProblemRepository;

  @Test
  @Transactional
  @Rollback(false) // DB 반영을 위해 false 설정
  void injectWrongReasons() throws JsonProcessingException {
    // 1. user1의 모든 MemberProblem 조회
    List<MemberProblem> memberProblems = memberProblemRepository.findByMemberUsername("user1");

    Random random = new Random();
    String[] dummyReasons = {
        "시간 복잡도 초과", "인덱스 범위 에러", "조건문 논리 오류",
        "메모리 초과", "초기화 누락", "잘못된 자료형 사용"
    };

    System.out.println("데이터 주입 시작... 대상 문제 수: " + memberProblems.size());

    for (MemberProblem mp : memberProblems) {
      // 한 문제당 3개의 실패 사유 생성
      for (int i = 0; i < 3; i++) {
        // 2. 1536차원 랜덤 벡터 생성 (text-embedding-3-small 규격)
        float[] vector = new float[1536];
        for (int j = 0; j < 1536; j++) {
          // 코사인 유사도 연산 부하를 위해 -1.0 ~ 1.0 사이의 값 채움
          vector[j] = -1.0f + random.nextFloat() * 2.0f;
        }

        // 3. JSON 직렬화
        String embeddingJson = objectMapper.writeValueAsString(vector);

        // 랜덤 사유 선택
        String reasonText = dummyReasons[random.nextInt(dummyReasons.length)] + " (Test #" + i + ")";

        // 4. WrongReason 객체 생성 (생성자 파라미터 확인 필요)
        // public WrongReason(MemberProblem memberProblem, String reasonText, String embeddingJson)
        WrongReason wrongReason = new WrongReason(mp, reasonText, embeddingJson);

        // 5. 저장 (엔티티 매니저 혹은 레포지토리 사용)
        mp.getWrongReasons().add(wrongReason);
      }
    }

    System.out.println("총 " + (memberProblems.size() * 3) + "개의 벡터 데이터 주입 완료!");
  }
}
