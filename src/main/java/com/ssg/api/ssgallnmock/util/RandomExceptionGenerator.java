package com.ssg.api.ssgallnmock.util;

import org.springframework.stereotype.Component;
import java.util.Random;

@Component
public class RandomExceptionGenerator {
    
    private final Random random = new Random();
    
    // 30% 확률로 예외 발생
    private static final double EXCEPTION_PROBABILITY = 0.3;
    
    public void throwRandomExceptionIfNeeded() {
        if (random.nextDouble() < EXCEPTION_PROBABILITY) {
            int exceptionType = random.nextInt(4);
            
            switch (exceptionType) {
                case 0:
                    throw new RuntimeException("제휴사 서버 일시적 오류가 발생했습니다.");
                case 1:
                    throw new RuntimeException("제휴사 API 인증 실패");
                case 2:
                    throw new RuntimeException("제휴사 재고 부족으로 주문을 처리할 수 없습니다.");
                case 3:
                    throw new RuntimeException("제휴사 시스템 점검 중입니다.");
                default:
                    throw new RuntimeException("알 수 없는 오류가 발생했습니다.");
            }
        }
    }
    

} 