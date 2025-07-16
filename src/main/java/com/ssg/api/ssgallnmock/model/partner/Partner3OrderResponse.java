package com.ssg.api.ssgallnmock.model.partner;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Partner3OrderResponse {
    private String code;                 // 코드
    private String description;          // 설명
    private String affiliateOrderNumber; // 제휴사 주문번호
    private String orderState;           // 주문상태
}