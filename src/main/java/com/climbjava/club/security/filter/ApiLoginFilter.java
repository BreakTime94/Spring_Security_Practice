package com.climbjava.club.security.filter;

import com.climbjava.club.security.dto.ClubAuthMemberDTO;
import com.climbjava.club.security.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.io.IOException;
import java.io.PrintWriter;

@Log4j2
public class ApiLoginFilter extends AbstractAuthenticationProcessingFilter {

  private JWTUtil jwtUtil;

  public ApiLoginFilter(String defaultFilterProcessesUrl, JWTUtil jwtUtil) {
    super(defaultFilterProcessesUrl);
    this.jwtUtil = jwtUtil;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
    log.info("========================ApiLoginFilter 영역=================================");
    log.info("========================ApiLoginFilter 영역=================================");
    log.info("========================ApiLoginFilter 영역=================================");
    String email = request.getParameter("email");
    String pw = request.getParameter("pw");

    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, pw);

    return getAuthenticationManager().authenticate(token);
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
    log.info("========================");
    log.info("ApiLoginFilter에서 성공적으로 인증을 완료했다능");

//    //SecurityContext 생성/설정
//    SecurityContext context = SecurityContextHolder.createEmptyContext();
//    context.setAuthentication(authResult);
//    SecurityContextHolder.setContext(context);
//
//    //이거를 session에 저장하여야 한다.
//    request.getSession(true).setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
//    response.sendRedirect("/");

    String email = ((ClubAuthMemberDTO)authResult.getPrincipal()).getUsername();

    String token = null;
    try{
      token = jwtUtil.generateToken(email);
      response.setContentType("text/plain"); //Token은 String 데이터입니다.
      response.getOutputStream().write(token.getBytes());
    } catch (Exception e){
      e.printStackTrace();
    }

  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
    log.info("========================");
    log.info("ApiLoginFilter에서 인증을 실패했다능");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    JSONObject json = new JSONObject();
    String msg = failed.getMessage();
    json.put("msg", msg);
    json.put("code", "401");

    PrintWriter out = response.getWriter();
    out.print(json);
  }
}
