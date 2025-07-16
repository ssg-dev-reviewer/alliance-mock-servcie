package com.ssg.api.ssgallnmock.model.partner;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Partner1StatusResponse {
    private String resultCode;           // 결과코드
    private String resultMessage;        // 결과메시지
    private String orderStatus;          // 주문상태
    private String lastUpdateTime;       // 최종업데이트시간
    private String deliveryCompany;      // 배송업체 (제휴사1만의 추가 필드)
} 