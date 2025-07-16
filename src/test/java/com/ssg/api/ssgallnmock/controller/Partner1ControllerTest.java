package com.ssg.api.ssgallnmock.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssg.api.ssgallnmock.exception.GlobalExceptionHandler;
import com.ssg.api.ssgallnmock.model.partner.Partner1OrderRequest;
import com.ssg.api.ssgallnmock.model.partner.Partner1OrderResponse;
import com.ssg.api.ssgallnmock.model.partner.Partner1StatusResponse;
import com.ssg.api.ssgallnmock.util.RandomExceptionGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class Partner1ControllerTest {

    @Mock
    private RandomExceptionGenerator exceptionGenerator;

    @InjectMocks
    private Partner1Controller partner1Controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(partner1Controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void createOrder_Success() throws Exception {
        // Given
        Partner1OrderRequest.Partner1OrderItem item = new Partner1OrderRequest.Partner1OrderItem(
            "P001", "상품1", 2, 15000.0
        );
        Partner1OrderRequest request = new Partner1OrderRequest(
            "ORDER001", LocalDateTime.now(), Arrays.asList(item)
        );

        doNothing().when(exceptionGenerator).throwRandomExceptionIfNeeded();

        // When & Then
        mockMvc.perform(post("/api/partner1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.resultMessage").value("주문이 성공적으로 생성되었습니다."))
                .andExpect(jsonPath("$.partnerOrderNumber").exists())
                .andExpect(jsonPath("$.orderStatus").value("주문접수"));

        verify(exceptionGenerator, times(1)).throwRandomExceptionIfNeeded();
    }

    @Test
    void createOrder_ThrowsException() throws Exception {
        // Given
        Partner1OrderRequest.Partner1OrderItem item = new Partner1OrderRequest.Partner1OrderItem(
            "P001", "상품1", 1, 15000.0
        );
        Partner1OrderRequest request = new Partner1OrderRequest(
            "ORDER002", LocalDateTime.now(), Arrays.asList(item)
        );

        doThrow(new RuntimeException("제휴사 서버 일시적 오류가 발생했습니다."))
                .when(exceptionGenerator).throwRandomExceptionIfNeeded();

        // When & Then
        mockMvc.perform(post("/api/partner1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("제휴사 서버 일시적 오류가 발생했습니다."))
                .andExpect(jsonPath("$.status").value(500));

        verify(exceptionGenerator, times(1)).throwRandomExceptionIfNeeded();
    }

    @Test
    void getOrderStatus_Success() throws Exception {
        // Given
        String orderNumber = "P1_1234567890";
        doNothing().when(exceptionGenerator).throwRandomExceptionIfNeeded();

        // When & Then
        mockMvc.perform(get("/api/partner1/orders/{orderNumber}/status", orderNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.resultMessage").value("주문 상태 조회 성공"))
                .andExpect(jsonPath("$.orderStatus").exists())
                .andExpect(jsonPath("$.lastUpdateTime").exists())
                .andExpect(jsonPath("$.deliveryCompany").value("CJ배송"));

        verify(exceptionGenerator, times(1)).throwRandomExceptionIfNeeded();
    }

    @Test
    void getOrderStatus_ThrowsException() throws Exception {
        // Given
        String orderNumber = "P1_1234567890";
        doThrow(new RuntimeException("제휴사 API 인증 실패"))
                .when(exceptionGenerator).throwRandomExceptionIfNeeded();

        // When & Then
        mockMvc.perform(get("/api/partner1/orders/{orderNumber}/status", orderNumber))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("제휴사 API 인증 실패"))
                .andExpect(jsonPath("$.status").value(500));

        verify(exceptionGenerator, times(1)).throwRandomExceptionIfNeeded();
    }

    @Test
    void createOrder_WithMultipleItems() throws Exception {
        // Given
        Partner1OrderRequest.Partner1OrderItem item1 = new Partner1OrderRequest.Partner1OrderItem(
            "P001", "상품1", 2, 15000.0
        );
        Partner1OrderRequest.Partner1OrderItem item2 = new Partner1OrderRequest.Partner1OrderItem(
            "P002", "상품2", 1, 25000.0
        );
        Partner1OrderRequest request = new Partner1OrderRequest(
            "ORDER003", LocalDateTime.now(), Arrays.asList(item1, item2)
        );

        doNothing().when(exceptionGenerator).throwRandomExceptionIfNeeded();

        // When & Then
        mockMvc.perform(post("/api/partner1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.partnerOrderNumber").exists());

        verify(exceptionGenerator, times(1)).throwRandomExceptionIfNeeded();
    }
} 