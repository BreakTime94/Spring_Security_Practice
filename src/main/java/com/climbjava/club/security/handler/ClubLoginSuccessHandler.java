package com.climbjava.club.security.handler;

import com.climbjava.club.security.dto.ClubAuthMemberDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Log4j2
public class ClubLoginSuccessHandler implements AuthenticationSuccessHandler {

  private final PasswordEncoder passwordEncoder;
  private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

  public ClubLoginSuccessHandler(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    log.info("ClubLoginSuccessHandler일세");
    log.info("onAuthenticationSuccess");

    ClubAuthMemberDTO authMemberDTO = (ClubAuthMemberDTO) authentication.getPrincipal();

    boolean fromSocial = authMemberDTO.isFromSocial();

    log.info("Need Modify Member?:{}", fromSocial);

    boolean passwordResult = passwordEncoder.matches("1111", authMemberDTO.getPassword());

    if(fromSocial && passwordResult){
      redirectStrategy.sendRedirect(request, response, "/member/modify?from=social");
    }

  }
}
