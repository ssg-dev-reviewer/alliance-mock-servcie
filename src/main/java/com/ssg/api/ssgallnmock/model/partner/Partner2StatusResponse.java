package com.ssg.api.ssgallnmock.model.partner;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Partner2StatusResponse {
    private String status;                // 상태
    private String message;               // 메시지
    private String currentStatus;         // 현재상태
    private String updatedAt;             // 업데이트시간
    private String processingCenter;      // 처리센터 (제휴사2만의 추가 필드)
} 