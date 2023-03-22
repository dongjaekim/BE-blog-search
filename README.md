# 블로그 검색 API

---

## JAR 다운로드 링크

- https://github.com/dongjaekim/BE-blog-search/raw/main/jar/blog-api-0.0.1-SNAPSHOT.jar

## API 명세서
jar 파일 실행 및 아래 링크에서 스웨거를 통해 API 명세를 확인하실 수 있습니다.
- http://localhost:8080/swagger-ui/index.html

## 프로젝트 설정

- JAVA 11 사용
- Spring Boot 2.7.7
- Gradle 7.6.1
- blog-api, blog-core 멀티모듈 구성
- H2 메모리 DB 사용

## 외부 라이브러리 목록
### Lombok

- getter, setter 등의 반복되는 메서드 코드를 어노테이션을 통해 생성해주기 위해 사용

### MapStruct

- DTO와 엔티티 간의 맵핑을 위해 사용

### Spring Cloud OpenFeign

- 외부 API를 호출해주는 클라이언트로 사용

### Swagger

- API 명세 자동화 및 테스트할 수 있는 UI 제공