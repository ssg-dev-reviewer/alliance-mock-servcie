package com.ssg.api.ssgallnmock.controller;

import com.ssg.api.ssgallnmock.model.partner.Partner3OrderRequest;
import com.ssg.api.ssgallnmock.model.partner.Partner3OrderResponse;
import com.ssg.api.ssgallnmock.model.partner.Partner3StatusResponse;
import com.ssg.api.ssgallnmock.util.RandomExceptionGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@RestController
@RequestMapping("/api/partner3")
public class Partner3Controller {
    
    @Autowired
    private RandomExceptionGenerator exceptionGenerator;
    
    private final Random random = new Random();
    
    // 주문 생성 API
    @PostMapping("/orders")
    public ResponseEntity<Partner3OrderResponse> createOrder(@RequestBody Partner3OrderRequest request) {
        // 랜덤 예외 발생
        exceptionGenerator.throwRandomExceptionIfNeeded();
        
        // 성공 응답 생성
        Partner3OrderResponse response = new Partner3OrderResponse();
        response.setCode("200");
        response.setDescription("주문이 성공적으로 생성되었습니다.");
        response.setAffiliateOrderNumber("P3_" + System.currentTimeMillis());
        response.setOrderState("RECEIVED");

        
        return ResponseEntity.ok(response);
    }
    
    // 주문 상태 조회 API
    @GetMapping("/orders/{transactionNumber}/status")
    public ResponseEntity<Partner3StatusResponse> getOrderStatus(@PathVariable String transactionNumber) {
        // 랜덤 예외 발생
        exceptionGenerator.throwRandomExceptionIfNeeded();
        
        // 랜덤 주문 상태 생성
        String[] statuses = {"RECEIVED", "PREPARING", "IN_TRANSIT", "DELIVERED", "CANCELLED"};
        String randomStatus = statuses[random.nextInt(statuses.length)];
        
        Partner3StatusResponse response = new Partner3StatusResponse();
        response.setCode("200");
        response.setDescription("주문 상태 조회 성공");
        response.setOrderState(randomStatus);
        response.setModifiedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        response.setShippingMethod("택배");
        
        return ResponseEntity.ok(response);
    }
} 