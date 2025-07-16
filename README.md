# SSG 제휴사 Mock 서비스

이 프로젝트는 SSG 제휴 중계 서비스 과제를 위한 제휴사 Mock API 서버입니다.  
3개의 제휴사(MA0001, MA0002, MA0003)에 대한 Mock API를 제공하며, 각 제휴사별로 다른 요청/응답 포맷과 랜덤 예외 발생 기능을 포함합니다.

## 프로젝트 구조

```
src/main/java/com/ssg/api/ssgallnmock/
├── controller/           # 제휴사별 API 컨트롤러
│   ├── Partner1Controller.java    # 제휴사1 (MA0001) API
│   ├── Partner2Controller.java    # 제휴사2 (MA0002) API
│   └── Partner3Controller.java    # 제휴사3 (MA0003) API
├── model/partner/       # 제휴사별 요청/응답 모델
│   ├── Partner1OrderRequest.java      # 제휴사1 주문 요청
│   ├── Partner1OrderResponse.java     # 제휴사1 주문 응답
│   ├── Partner1StatusResponse.java    # 제휴사1 상태 조회 응답
│   ├── Partner2OrderRequest.java      # 제휴사2 주문 요청
│   ├── Partner2OrderResponse.java     # 제휴사2 주문 응답
│   ├── Partner2StatusResponse.java    # 제휴사2 상태 조회 응답
│   ├── Partner3OrderRequest.java      # 제휴사3 주문 요청
│   ├── Partner3OrderResponse.java     # 제휴사3 주문 응답
│   └── Partner3StatusResponse.java    # 제휴사3 상태 조회 응답
├── exception/           # 예외 처리
│   └── GlobalExceptionHandler.java    # 전역 예외 처리
├── util/               # 유틸리티
│   └── RandomExceptionGenerator.java  # 랜덤 예외 발생
└── SsgAllnMockApplication.java        # 메인 애플리케이션
```

## 구현 방향

### 1. 제휴사별 다른 API 포맷
각 제휴사는 서로 다른 요청/응답 포맷을 사용하여 실제 제휴사 환경을 시뮬레이션합니다.

자세한 사항은 제휴사별요청응답_포맷.md를 참고하세요.

### 2. 랜덤 예외 발생
각 API 호출 시 30% 확률로 다양한 예외 상황을 발생시켜 실제 운영 환경의 불안정성을 시뮬레이션합니다.

#### 발생 가능한 예외 종류
- 서버 일시적 오류
- API 인증 실패
- 재고 부족으로 인한 주문 처리 불가
- 시스템 점검 중

### 3. 각 제휴사별 고유 필드
각 제휴사는 자체적인 추가 필드를 가지고 있어 실제 제휴사 API의 다양성을 반영합니다.

## 빌드 및 실행 방법

### 1. 프로젝트 빌드
```bash
# 전체 빌드 (테스트 포함)
./gradlew build

# 테스트 제외하고 빌드
./gradlew build -x test
```

### 2. 애플리케이션 실행
```bash
# Gradle을 통한 실행
./gradlew bootRun

# 또는 JAR 파일로 실행
java -jar build/libs/ssg-alln-mock-0.0.1-SNAPSHOT.jar
```

### 3. 서버 접속 및 확인
- **서버 URL**: `http://localhost:9710`

## API 명세
자세한 사항은 **제휴사별요청응답_포맷.md**를 참고하세요.

## 테스트 방법

### CURL을 이용한 테스트

#### 제휴사1 테스트
**주문 생성:**
```bash
curl -X POST http://localhost:9710/api/partner1/orders \
  -H "Content-Type: application/json" \
  -d '{
  "orderNumber": "ORDER001",
  "orderDate": "2024-01-15T10:30:00",
  "items": [
    {
      "itemCode": "P001",
      "itemName": "상품1",
      "orderQty": 2,
      "price": 15000.0
    }
  ]
}'
```

**주문 상태 조회:**
```bash
curl -X GET http://localhost:9710/api/partner1/orders/ORDER001/status
```

#### 제휴사2 테스트
**주문 생성:**
```bash
curl -X POST http://localhost:9710/api/partner2/orders \
  -H "Content-Type: application/json" \
  -d '{
  "orderId": "ORDER002",
  "orderTimestamp": "2024-01-15T10:30:00",
  "productList": [
    {
      "sku": "ITEM001",
      "name": "상품1",
      "qty": 3,
      "unitCost": 25000.0
    }
  ]
}'
```

**주문 상태 조회:**
```bash
curl -X GET http://localhost:9710/api/partner2/orders/ORDER002/status
```

#### 제휴사3 테스트
**주문 생성:**
```bash
curl -X POST http://localhost:9710/api/partner3/orders \
  -H "Content-Type: application/json" \
  -d '{
  "transactionNumber": "TXN001",
  "createdAt": "2024-01-15T10:30:00",
  "orderDetails": [
    {
      "productId": "PROD001",
      "productTitle": "상품1",
      "amount": 5,
      "cost": 12000.0
    }
  ]
}'
```

**주문 상태 조회:**
```bash
curl -X GET http://localhost:9710/api/partner3/orders/ORDER003/status
```


## 예외 처리

### 랜덤 예외 발생
각 API 호출 시 30% 확률로 다양한 예외 상황이 발생합니다.

#### 예외 종류
1. **서버 일시적 오류**: "제휴사 서버 일시적 오류가 발생했습니다."
2. **API 인증 실패**: "제휴사 API 인증 실패"
3. **재고 부족**: "제휴사 재고 부족으로 주문을 처리할 수 없습니다."
4. **시스템 점검**: "제휴사 시스템 점검 중입니다."

#### 예외 응답 예시
```json
{
  "timestamp": "2024-01-15T14:30:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "제휴사 재고 부족으로 주문을 처리할 수 없습니다.",
  "path": "/api/partner"
}
```

### 예외 처리 특징
- 모든 예외는 HTTP 500 상태 코드로 응답
- 일관된 JSON 에러 포맷 제공
- 타임스탬프, 에러 메시지, 경로 정보 포함


## 개발 환경

- **Java**: 11
- **Spring Boot**: 2.7.18
- **Gradle**: 8.x
- **Lombok**: 최신 버전
- **서버 포트**: 9710

이 프로젝트는 SSG 제휴 중계 서비스 과제용으로 제작되었습니다. 
