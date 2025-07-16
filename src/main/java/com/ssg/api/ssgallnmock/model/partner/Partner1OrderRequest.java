package com.ssg.api.ssgallnmock.model.partner;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Partner1OrderRequest {
    private String orderNumber;         // 주문번호
    private LocalDateTime orderDate;    // 주문일시
    private List<Partner1OrderItem> items; // 주문상품 리스트
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Partner1OrderItem {
        private String itemCode;        // 상품코드
        private String itemName;        // 상품명
        private Integer orderQty;       // 주문수량
        private Double price;           // 단가
    }
} 