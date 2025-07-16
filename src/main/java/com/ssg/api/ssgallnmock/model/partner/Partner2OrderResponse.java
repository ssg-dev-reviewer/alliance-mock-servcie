package com.ssg.api.ssgallnmock.model.partner;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Partner2OrderResponse {
    private String status;               // 상태
    private String message;              // 메시지
    private String merchantOrderId;      // 제휴사 주문ID
    private String currentStatus;        // 현재상태
}