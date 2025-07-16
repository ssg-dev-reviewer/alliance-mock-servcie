package com.ssg.api.ssgallnmock.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssg.api.ssgallnmock.exception.GlobalExceptionHandler;
import com.ssg.api.ssgallnmock.model.partner.Partner3OrderRequest;
import com.ssg.api.ssgallnmock.model.partner.Partner3OrderResponse;
import com.ssg.api.ssgallnmock.model.partner.Partner3StatusResponse;
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
class Partner3ControllerTest {

    @Mock
    private RandomExceptionGenerator exceptionGenerator;

    @InjectMocks
    private Partner3Controller partner3Controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(partner3Controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void createOrder_Success() throws Exception {
        // Given
        Partner3OrderRequest.Partner3OrderItem item = new Partner3OrderRequest.Partner3OrderItem(
            "PROD001", "상품1", 5, 12000.0
        );
        Partner3OrderRequest request = new Partner3OrderRequest(
            "TXN001", LocalDateTime.now(), Arrays.asList(item)
        );

        doNothing().when(exceptionGenerator).throwRandomExceptionIfNeeded();

        // When & Then
        mockMvc.perform(post("/api/partner3/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.description").value("주문이 성공적으로 생성되었습니다."))
                .andExpect(jsonPath("$.affiliateOrderNumber").exists())
                .andExpect(jsonPath("$.orderState").value("RECEIVED"));

        verify(exceptionGenerator, times(1)).throwRandomExceptionIfNeeded();
    }

    @Test
    void createOrder_ThrowsException() throws Exception {
        // Given
        Partner3OrderRequest.Partner3OrderItem item = new Partner3OrderRequest.Partner3OrderItem(
            "PROD001", "상품1", 1, 12000.0
        );
        Partner3OrderRequest request = new Partner3OrderRequest(
            "TXN002", LocalDateTime.now(), Arrays.asList(item)
        );

        doThrow(new RuntimeException("제휴사 서버 일시적 오류가 발생했습니다."))
                .when(exceptionGenerator).throwRandomExceptionIfNeeded();

        // When & Then
        mockMvc.perform(post("/api/partner3/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("제휴사 서버 일시적 오류가 발생했습니다."));

        verify(exceptionGenerator, times(1)).throwRandomExceptionIfNeeded();
    }

    @Test
    void getOrderStatus_Success() throws Exception {
        // Given
        String transactionNumber = "P3_1234567890";
        doNothing().when(exceptionGenerator).throwRandomExceptionIfNeeded();

        // When & Then
        mockMvc.perform(get("/api/partner3/orders/{transactionNumber}/status", transactionNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.description").value("주문 상태 조회 성공"))
                .andExpect(jsonPath("$.orderState").exists())
                .andExpect(jsonPath("$.modifiedAt").exists())
                .andExpect(jsonPath("$.shippingMethod").value("택배"));

        verify(exceptionGenerator, times(1)).throwRandomExceptionIfNeeded();
    }

    @Test
    void getOrderStatus_ThrowsException() throws Exception {
        // Given
        String transactionNumber = "P3_1234567890";
        doThrow(new RuntimeException("제휴사 API 인증 실패"))
                .when(exceptionGenerator).throwRandomExceptionIfNeeded();

        // When & Then
        mockMvc.perform(get("/api/partner3/orders/{transactionNumber}/status", transactionNumber))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("제휴사 API 인증 실패"));

        verify(exceptionGenerator, times(1)).throwRandomExceptionIfNeeded();
    }

    @Test
    void createOrder_WithMultipleItems() throws Exception {
        // Given
        Partner3OrderRequest.Partner3OrderItem item1 = new Partner3OrderRequest.Partner3OrderItem(
            "PROD001", "상품1", 3, 12000.0
        );
        Partner3OrderRequest.Partner3OrderItem item2 = new Partner3OrderRequest.Partner3OrderItem(
            "PROD002", "상품2", 2, 18000.0
        );
        Partner3OrderRequest request = new Partner3OrderRequest(
            "TXN003", LocalDateTime.now(), Arrays.asList(item1, item2)
        );

        doNothing().when(exceptionGenerator).throwRandomExceptionIfNeeded();

        // When & Then
        mockMvc.perform(post("/api/partner3/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.affiliateOrderNumber").exists());

        verify(exceptionGenerator, times(1)).throwRandomExceptionIfNeeded();
    }
} 