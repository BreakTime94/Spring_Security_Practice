package com.climbjava.club.security;

import com.climbjava.club.security.util.JWTUtil;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class JWTTests {

  private JWTUtil util;

  @BeforeEach //JWTTEST를 테스트 할 때마다 먼저 실행되는 메서드
  public void testBefore() {
    util = new JWTUtil();
  }

  @Test
  public void testEncoder() throws Exception {
    String email = "user100@gmail.com";

    String str = util.generateToken(email);

    log.info(str);
  }

}
