package com.ssg.api.ssgallnmock.model.partner;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Partner3OrderRequest {
    private String transactionNumber;   // 거래번호
    private LocalDateTime createdAt;    // 생성일시
    private List<Partner3OrderItem> orderDetails; // 주문상세
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Partner3OrderItem {
        private String productId;       // 상품ID
        private String productTitle;    // 상품제목
        private Integer amount;         // 수량
        private Double cost;            // 비용
    }
} 