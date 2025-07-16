package com.ssg.api.ssgallnmock.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssg.api.ssgallnmock.exception.GlobalExceptionHandler;
import com.ssg.api.ssgallnmock.model.partner.Partner2OrderRequest;
import com.ssg.api.ssgallnmock.model.partner.Partner2OrderResponse;
import com.ssg.api.ssgallnmock.model.partner.Partner2StatusResponse;
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
class Partner2ControllerTest {

    @Mock
    private RandomExceptionGenerator exceptionGenerator;

    @InjectMocks
    private Partner2Controller partner2Controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(partner2Controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }
    @Test
    void createOrder_Success() throws Exception {
        // Given
        Partner2OrderRequest.Partner2OrderItem item = new Partner2OrderRequest.Partner2OrderItem(
            "ITEM001", "상품1", 3, 25000.0
        );
        Partner2OrderRequest request = new Partner2OrderRequest(
            "ORDER001", LocalDateTime.now().toString(), Arrays.asList(item)
        );

        doNothing().when(exceptionGenerator).throwRandomExceptionIfNeeded();

        // When & Then
        mockMvc.perform(post("/api/partner2/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("주문이 성공적으로 생성되었습니다."))
                .andExpect(jsonPath("$.merchantOrderId").exists())
                .andExpect(jsonPath("$.currentStatus").value("ORDER_RECEIVED"));

        verify(exceptionGenerator, times(1)).throwRandomExceptionIfNeeded();
    }

    @Test
    void createOrder_ThrowsException() throws Exception {
        // Given
        Partner2OrderRequest.Partner2OrderItem item = new Partner2OrderRequest.Partner2OrderItem(
            "ITEM001", "상품1", 1, 25000.0
        );
        Partner2OrderRequest request = new Partner2OrderRequest(
            "ORDER002", LocalDateTime.now().toString(), Arrays.asList(item)
        );

        doThrow(new RuntimeException("제휴사 재고 부족으로 주문을 처리할 수 없습니다."))
                .when(exceptionGenerator).throwRandomExceptionIfNeeded();

        // When & Then
        mockMvc.perform(post("/api/partner2/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("제휴사 재고 부족으로 주문을 처리할 수 없습니다."));

        verify(exceptionGenerator, times(1)).throwRandomExceptionIfNeeded();
    }

    @Test
    void getOrderStatus_Success() throws Exception {
        // Given
        String orderId = "P2_1234567890";
        doNothing().when(exceptionGenerator).throwRandomExceptionIfNeeded();

        // When & Then
        mockMvc.perform(get("/api/partner2/orders/{orderId}/status", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("주문 상태 조회 성공"))
                .andExpect(jsonPath("$.currentStatus").exists())
                .andExpect(jsonPath("$.updatedAt").exists())
                .andExpect(jsonPath("$.processingCenter").value("부산처리센터"));

        verify(exceptionGenerator, times(1)).throwRandomExceptionIfNeeded();
    }

    @Test
    void getOrderStatus_ThrowsException() throws Exception {
        // Given
        String orderId = "P2_1234567890";
        doThrow(new RuntimeException("제휴사 시스템 점검 중입니다."))
                .when(exceptionGenerator).throwRandomExceptionIfNeeded();

        // When & Then
        mockMvc.perform(get("/api/partner2/orders/{orderId}/status", orderId))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("제휴사 시스템 점검 중입니다."));

        verify(exceptionGenerator, times(1)).throwRandomExceptionIfNeeded();
    }



    @Test
    void createOrder_WithMultipleItems() throws Exception {
        // Given
        Partner2OrderRequest.Partner2OrderItem item1 = new Partner2OrderRequest.Partner2OrderItem(
            "ITEM001", "상품1", 2, 25000.0
        );
        Partner2OrderRequest.Partner2OrderItem item2 = new Partner2OrderRequest.Partner2OrderItem(
            "ITEM002", "상품2", 1, 35000.0
        );
        Partner2OrderRequest request = new Partner2OrderRequest(
            "ORDER003", LocalDateTime.now().toString(), Arrays.asList(item1, item2)
        );

        doNothing().when(exceptionGenerator).throwRandomExceptionIfNeeded();

        // When & Then
        mockMvc.perform(post("/api/partner2/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.merchantOrderId").exists());

        verify(exceptionGenerator, times(1)).throwRandomExceptionIfNeeded();
    }
} 