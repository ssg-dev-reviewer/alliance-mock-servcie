package com.ssg.api.ssgallnmock.model.partner;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Partner3StatusResponse {
    private String code;                  // 코드
    private String description;           // 설명
    private String orderState;            // 주문상태
    private String modifiedAt;            // 수정시간
    private String shippingMethod;        // 배송방법 (제휴사3만의 추가 필드)
} 