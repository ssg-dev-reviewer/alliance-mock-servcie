package com.ssg.api.ssgallnmock.controller;

import com.ssg.api.ssgallnmock.model.partner.Partner1OrderRequest;
import com.ssg.api.ssgallnmock.model.partner.Partner1OrderResponse;
import com.ssg.api.ssgallnmock.model.partner.Partner1StatusResponse;
import com.ssg.api.ssgallnmock.util.RandomExceptionGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@RestController
@RequestMapping("/api/partner1")
public class Partner1Controller {
    
    @Autowired
    private RandomExceptionGenerator exceptionGenerator;
    
    private final Random random = new Random();
    
    // 주문 생성 API
    @PostMapping("/orders")
    public ResponseEntity<Partner1OrderResponse> createOrder(@RequestBody Partner1OrderRequest request) {
        // 랜덤 예외 발생
        exceptionGenerator.throwRandomExceptionIfNeeded();
        
        // 성공 응답 생성
        Partner1OrderResponse response = new Partner1OrderResponse();
        response.setResultCode("SUCCESS");
        response.setResultMessage("주문이 성공적으로 생성되었습니다.");
        response.setPartnerOrderNumber("P1_" + System.currentTimeMillis());
        response.setOrderStatus("주문접수");

        return ResponseEntity.ok(response);
    }
    
    // 주문 상태 조회 API
    @GetMapping("/orders/{orderNumber}/status")
    public ResponseEntity<Partner1StatusResponse> getOrderStatus(@PathVariable String orderNumber) {
        // 랜덤 예외 발생
        exceptionGenerator.throwRandomExceptionIfNeeded();
        
        // 랜덤 주문 상태 생성
        String[] statuses = {"주문접수", "상품준비중", "배송중", "배송완료", "주문취소"};
        String randomStatus = statuses[random.nextInt(statuses.length)];
        
        Partner1StatusResponse response = new Partner1StatusResponse();
        response.setResultCode("SUCCESS");
        response.setResultMessage("주문 상태 조회 성공");
        response.setOrderStatus(randomStatus);
        response.setLastUpdateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        response.setDeliveryCompany("CJ배송");
        
        return ResponseEntity.ok(response);
    }
} 