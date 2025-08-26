package com.climbjava.club.security.filter;

import com.climbjava.club.security.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Log4j2
public class ApiCheckFilter extends OncePerRequestFilter {

  private AntPathMatcher antPathMatcher;
  private String pattern;
  private JWTUtil jwtUtil;

  public ApiCheckFilter(String pattern, JWTUtil jwtUtil) {
    this.antPathMatcher = new AntPathMatcher();
    this.pattern = pattern;
    this.jwtUtil = jwtUtil;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    log.info("=====여기가 doFilter의 영역이다=====");
    log.info("request의 URI를 체크해본다.:{}",request.getRequestURI());
    log.info("request.getRequestURI가 일치하냐?{}" ,antPathMatcher.match(pattern, request.getRequestURI()));
    if(antPathMatcher.match(pattern, request.getRequestURI())){
      log.info("ApiCheckFilter에 if 문 안으로 들어왔다. (pattern 이랑 request 안에 담겨있는 URI랑 같은 경우임)");
      log.info("ApiCheckFilter에 if 문 안으로 들어왔다. (pattern 이랑 request 안에 담겨있는 URI랑 같은 경우임)");
      log.info("ApiCheckFilter에 if 문 안으로 들어왔다. (pattern 이랑 request 안에 담겨있는 URI랑 같은 경우임)");

      boolean checkHeader = checkAuthHeader(request);

      if(checkHeader){
        log.info("checkHeader True ㅋㅋ");
        filterChain.doFilter(request,response);
        return;
      }else{
        log.info("checkHeader False ㅋㅋ");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        //json 리턴 및 한글깨짐 수정.
        response.setContentType("application/json;charset=UTF-8");
        JSONObject json = new JSONObject();
        String message = "FAIL CHECK API TOKEN";

        json.put("code", "403");
        json.put("message", message);

        PrintWriter out = response.getWriter();
        out.print(json);
        return;
      }
    }
    filterChain.doFilter(request, response);
  }

  private boolean checkAuthHeader(HttpServletRequest request) {
    boolean checkResult = false;
    String authHeader = request.getHeader("Authorization");
//    if(StringUtils.hasText(authHeader)){
//      log.info("Authorization exist(권한 있냐능) : " + authHeader);
//      if(authHeader.equals("12345678")){
//        log.info("짜샤 축하한다. AuthHeader 일치해");
//        checkResult = true;
//      }
//    }
//    log.info("AuthHeader 안 일치하는데 너무 실망하지는 마");
    if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")){
      log.info("이것이 authHeader다 이 말이야 : {}", authHeader);

      try {
        String email = jwtUtil.validateAndExtract(authHeader.substring(7));
        log.info("올 유효한 이메일 추출함 {}", email);
        checkResult =!email.isEmpty();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return checkResult;
  }

}
