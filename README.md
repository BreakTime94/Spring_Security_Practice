# Spring_Board
![Spring](https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![SpringBoot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)

---
### 📅 250821 
- spring security 최초 시작 
- Club Entity, 기초 Repository, 기본 dummydata 작성 
- 기본 Spring Config 및 기본 thymeleaf 페이지 작성

### 📅 250822 
- Repository를 활용한 기초 쿼리 메서드 작성(findByEmail) 등 
- clubAuthMemberDTO class 생성, security <-> dto 관계 성립
- Google OAuth 가입 및 gradle 의존성 주입
- SecurityConfig 수정, OAuth 로그인 유저 DB주입(기본 pw 적용)
- 인증 성공후 처리 과정을 다룬 LoginSuccessHandler 작성 및 관련 Social 로그인 시 modify 페이지 이동(프론트 페이지 미구현)

### 📅 250825
- Note 등록 관련 Service 코드 작성
- Note RestController 작성(비동기 처리 방식), PostMan 활용 적용 테스트
- ApiCheckFilter, ApiLoginFilter를 통한 Filter 처리 순서 조정
- JWT 토큰 적용 준비

### 📅 250826
- JWT 토큰 기초 적용
- CORS Filter(Custom Filter) 적용
- PostMan 활용 (api/login 적용 및 JWT토큰 발급 체크, CORS Filter 적용 체크(요청 메서드 방식, Header 내 Authentication 적용여부 등 활용)
- CORS Filter를 적용한 간단 ReactComponent 활용 연습 중

