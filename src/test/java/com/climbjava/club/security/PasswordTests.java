package com.climbjava.club.security;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@Log4j2
public class PasswordTests {
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Test
  public void testExist() {
    Assertions.assertNotNull(passwordEncoder);
  }

  @Test
  public void testEncode() {
    String password = "1234";
    String enpw = passwordEncoder.encode(password);
    log.info("enpw : {}", enpw);

    boolean result = passwordEncoder.matches(password, enpw);
    log.info("result : {}", result);

    Assertions.assertTrue(result);

  }

}
