package com.ssg.api.ssgallnmock.model.partner;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Partner2OrderRequest {
    private String orderId;             // 주문ID
    private String orderTimestamp;      // 주문타임스탬프 (문자열)
    private List<Partner2OrderItem> productList; // 상품리스트
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Partner2OrderItem {
        private String sku;              // SKU
        private String name;             // 상품명
        private Integer qty;             // 수량
        private Double unitCost;         // 단가
    }
} 