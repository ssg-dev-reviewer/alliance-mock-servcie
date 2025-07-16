package com.ssg.api.ssgallnmock.controller;

import com.ssg.api.ssgallnmock.model.partner.Partner2OrderRequest;
import com.ssg.api.ssgallnmock.model.partner.Partner2OrderResponse;
import com.ssg.api.ssgallnmock.model.partner.Partner2StatusResponse;
import com.ssg.api.ssgallnmock.util.RandomExceptionGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@RestController
@RequestMapping("/api/partner2")
public class Partner2Controller {
    
    @Autowired
    private RandomExceptionGenerator exceptionGenerator;
    
    private final Random random = new Random();
    
    // 주문 생성 API
    @PostMapping("/orders")
    public ResponseEntity<Partner2OrderResponse> createOrder(@RequestBody Partner2OrderRequest request) {
        // 랜덤 예외 발생
        exceptionGenerator.throwRandomExceptionIfNeeded();
        
        // 성공 응답 생성
        Partner2OrderResponse response = new Partner2OrderResponse();
        response.setStatus("SUCCESS");
        response.setMessage("주문이 성공적으로 생성되었습니다.");
        response.setMerchantOrderId("P2_" + System.currentTimeMillis());
        response.setCurrentStatus("ORDER_RECEIVED");

        return ResponseEntity.ok(response);
    }
    
    // 주문 상태 조회 API
    @GetMapping("/orders/{orderId}/status")
    public ResponseEntity<Partner2StatusResponse> getOrderStatus(@PathVariable String orderId) {
        // 랜덤 예외 발생
        exceptionGenerator.throwRandomExceptionIfNeeded();
        
        // 랜덤 주문 상태 생성
        String[] statuses = {"ORDER_RECEIVED", "PROCESSING", "SHIPPING", "DELIVERED", "CANCELLED"};
        String randomStatus = statuses[random.nextInt(statuses.length)];
        
        Partner2StatusResponse response = new Partner2StatusResponse();
        response.setStatus("SUCCESS");
        response.setMessage("주문 상태 조회 성공");
        response.setCurrentStatus(randomStatus);
        response.setUpdatedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        response.setProcessingCenter("부산처리센터");
        
        return ResponseEntity.ok(response);
    }
} 