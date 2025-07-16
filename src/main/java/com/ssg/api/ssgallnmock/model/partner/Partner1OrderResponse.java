package com.ssg.api.ssgallnmock.model.partner;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Partner1OrderResponse {
    private String resultCode;          // 결과코드
    private String resultMessage;       // 결과메시지
    private String partnerOrderNumber;  // 제휴사 주문번호
    private String orderStatus;         // 주문상태
}