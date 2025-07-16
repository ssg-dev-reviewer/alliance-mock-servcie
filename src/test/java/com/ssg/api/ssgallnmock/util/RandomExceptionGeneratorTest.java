package com.ssg.api.ssgallnmock.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RandomExceptionGeneratorTest {

    @Mock
    private Random random;

    @InjectMocks
    private RandomExceptionGenerator exceptionGenerator;

    @BeforeEach
    void setUp() {
        // Mock Random 인스턴스를 주입하기 위해 reflection 사용
        try {
            java.lang.reflect.Field randomField = RandomExceptionGenerator.class.getDeclaredField("random");
            randomField.setAccessible(true);
            randomField.set(exceptionGenerator, random);
        } catch (Exception e) {
            fail("Random 필드 주입 실패: " + e.getMessage());
        }
    }

    @Test
    void throwRandomExceptionIfNeeded_WhenProbabilityIsLow_ShouldNotThrowException() {
        // Given
        when(random.nextDouble()).thenReturn(0.8); // 30% 확률보다 높음

        // When & Then
        assertDoesNotThrow(() -> exceptionGenerator.throwRandomExceptionIfNeeded());
    }

    @Test
    void throwRandomExceptionIfNeeded_WhenProbabilityIsHigh_ShouldThrowException() {
        // Given
        when(random.nextDouble()).thenReturn(0.1); // 30% 확률보다 낮음
        when(random.nextInt(4)).thenReturn(0);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> exceptionGenerator.throwRandomExceptionIfNeeded());
        
        assertEquals("제휴사 서버 일시적 오류가 발생했습니다.", exception.getMessage());
    }

    @Test
    void throwRandomExceptionIfNeeded_WhenExceptionTypeIs1_ShouldThrowAuthenticationException() {
        // Given
        when(random.nextDouble()).thenReturn(0.1);
        when(random.nextInt(4)).thenReturn(1);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> exceptionGenerator.throwRandomExceptionIfNeeded());
        
        assertEquals("제휴사 API 인증 실패", exception.getMessage());
    }

    @Test
    void throwRandomExceptionIfNeeded_WhenExceptionTypeIs2_ShouldThrowInventoryException() {
        // Given
        when(random.nextDouble()).thenReturn(0.1);
        when(random.nextInt(4)).thenReturn(2);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> exceptionGenerator.throwRandomExceptionIfNeeded());
        
        assertEquals("제휴사 재고 부족으로 주문을 처리할 수 없습니다.", exception.getMessage());
    }

    @Test
    void throwRandomExceptionIfNeeded_WhenExceptionTypeIs3_ShouldThrowMaintenanceException() {
        // Given
        when(random.nextDouble()).thenReturn(0.1);
        when(random.nextInt(4)).thenReturn(3);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> exceptionGenerator.throwRandomExceptionIfNeeded());
        
        assertEquals("제휴사 시스템 점검 중입니다.", exception.getMessage());
    }

    @Test
    void throwRandomExceptionIfNeeded_WhenExceptionTypeIsOutOfRange_ShouldThrowDefaultException() {
        // Given
        when(random.nextDouble()).thenReturn(0.1);
        when(random.nextInt(4)).thenReturn(5); // 범위를 벗어난 값

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> exceptionGenerator.throwRandomExceptionIfNeeded());
        
        assertEquals("알 수 없는 오류가 발생했습니다.", exception.getMessage());
    }

    @Test
    void throwRandomExceptionIfNeeded_WhenCalledMultipleTimes_ShouldWorkConsistently() {
        // Given
        when(random.nextDouble()).thenReturn(0.1, 0.8, 0.2); // 첫 번째와 세 번째는 예외 발생
        when(random.nextInt(4)).thenReturn(0, 1); // 예외 타입

        // When & Then
        // 첫 번째 호출 - 예외 발생
        RuntimeException exception1 = assertThrows(RuntimeException.class, 
            () -> exceptionGenerator.throwRandomExceptionIfNeeded());
        assertEquals("제휴사 서버 일시적 오류가 발생했습니다.", exception1.getMessage());

        // 두 번째 호출 - 예외 발생 안함
        assertDoesNotThrow(() -> exceptionGenerator.throwRandomExceptionIfNeeded());

        // 세 번째 호출 - 예외 발생
        RuntimeException exception2 = assertThrows(RuntimeException.class, 
            () -> exceptionGenerator.throwRandomExceptionIfNeeded());
        assertEquals("제휴사 API 인증 실패", exception2.getMessage());
    }

    @Test
    void throwRandomExceptionIfNeeded_WithRealRandom_ShouldWork() {
        // Given - 실제 Random 인스턴스 사용
        RandomExceptionGenerator realGenerator = new RandomExceptionGenerator();
        
        // When & Then - 여러 번 호출해도 예외가 발생하지 않을 수 있음 (랜덤이므로)
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    realGenerator.throwRandomExceptionIfNeeded();
                } catch (RuntimeException e) {
                    // 예외가 발생하면 정상적인 동작
                    assertNotNull(e.getMessage());
                    assertTrue(e.getMessage().contains("제휴사"));
                }
            }
        });
    }
} 