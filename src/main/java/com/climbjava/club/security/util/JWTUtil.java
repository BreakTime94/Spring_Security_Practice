package com.climbjava.club.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecureDigestAlgorithm;
import lombok.extern.log4j.Log4j2;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

@Log4j2
public class JWTUtil {
  //글자수 32자 이상 권장(HS256)
  private String secretKeyString = "club12345678club12345678club12345678";

  private long expire = 60 * 24 * 30L ; //단위: 분 (30일)

  public String generateToken(String content) throws Exception {

    //암호화 시키는 것
    SecureDigestAlgorithm alg = Jwts.SIG.HS256;
    byte[] keyBytes = secretKeyString.getBytes();
    Key key = Keys.hmacShaKeyFor(keyBytes);

    //HEADER로 전달되는 값을 Claims 클래스에 담아서 jwts에 전달
    Claims claims = Jwts.claims()
            .subject(content)
            .issuedAt(new Date())
            .expiration(Date.from(ZonedDateTime.now().plusMinutes(expire).toInstant()))
            .build();

    return Jwts.builder()
            .claims(claims)
            .signWith(key, alg)
            .compact();
  }

  //토큰의 유효성검사
  public String validateAndExtract(String tokenStr) throws Exception {
    byte[] keyBytes = secretKeyString.getBytes();
    SecretKey key = Keys.hmacShaKeyFor(keyBytes);
    Claims claims = Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(tokenStr)
            .getPayload();
    log.info("도대체 claim이 뭐냐 이 말이야{}", claims);
    log.info(claims);
    log.info(claims.getSubject());
    return claims.getSubject();
  }
}
