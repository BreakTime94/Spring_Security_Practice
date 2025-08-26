package com.climbjava.club.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.MethodNotAllowedException;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

// CORS: Cross-Origin Resource Sharing
// 서버의 주소 또는 포트가 다른경우 데이터를 주고 받을 수 있도록 합니다.

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CORSFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    //1. 허용하고자 하는 주소를 설정
    // "*" : 모든 주소를 허용
    // 2번째 인자에 스트링 한 개의 값만 허용
    //Origin -> 나를 접근하려는 외부포트 ex) localhost:3000 이런 느낌
    //만약 여러 개를 하고 싶다?
    //List<String> allowOrigin = Arrays.asList("http://localhost:3000", "http://localhost:4000");
    //String originHeader = request.getHeader("Origin"); // 나에게 접속하려 하는 origin 주소
    // if(allowOrigin.contains(originHeader)) 이런식으로 처리한다.

//    if(!"POST".equalsIgnoreCase(request.getMethod())) {
//      throw new AuthenticationException("너가 요청한 방식은 지원을 안혀 ~~: " + request.getMethod());
//    }

    response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");

    //2. key나 토큰을 사용하쉴? ex) JWT Token 등
    response.setHeader("Access-Control-Allow-Credentials", "true");

    //3. 외부리소스가 요청한 메서드의 종류 (4종류)
    response.setHeader("Access-Control-Allow-Methods", "*");

    //4. 지속시간 (초단위)
    response.setHeader("Access-Control-Max-Age", "3600");
    //preflight 유효시간 사전 설정  ...?
    // OPTIONS로 먼저 데이터를 전송하여 사전확인 작업
    //POST /api/data HTTP/1.1
    //Origin: http://localhost:3000
    //Content-Type: application/json
    //Authorization: Bearer ~~~~~

    response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Key, Authorization");
    // Origins : 요청 주소, 포트
    // X-Request-With: Ajax 요청(비동기 요청시 만들어지는 것) XMLhttpRequest
    // Content-Type : Body 내용
    // Accept : 응답 받을 데이터 형식(application/json, text/html, text/plain)
    // Key :  사용자 헤더 (브라우저가 생성하는 것이 아님)
    // Authorization : 인증정보를 담은 것(JWT 토큰 등)

    if("OPTIONS".equalsIgnoreCase(request.getMethod())) {
      response.setStatus(HttpServletResponse.SC_OK);
    } else {
      filterChain.doFilter(request, response);
    }
  }
}
