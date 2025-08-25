package com.climbjava.club.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;

import java.time.ZonedDateTime;
import java.util.Date;

@Log4j2
public class JWTUtil {
  private String secretKey = "club12345678";

  private Long expire = 60 * 24 * 30L ; //단위: 분 (30일)

  public String generateToken(String content) throws Exception {
    return Jwts.builder()
            .setIssuedAt(new Date())
            .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expire).toInstant()))
            .claim("sub", content)
            .signWith(SignatureAlgorithm.HS256, secretKey.getBytes("UTF-8"))
            .compact();
  }

  public String validateAndExtract(String tokenStr) throws Exception {
    String contentValue = null;
    try {
      
    } catch (Exception e) {
      e.printStackTrace();
      log.error(e.getMessage());
      contentValue = null;
    }

    return contentValue;
  }
}
