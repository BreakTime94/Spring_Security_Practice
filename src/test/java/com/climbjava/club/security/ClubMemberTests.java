package com.climbjava.club.security;

import com.climbjava.club.entity.ClubMember;
import com.climbjava.club.entity.ClubMemberRole;
import com.climbjava.club.repository.ClubMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.stream.IntStream;

@SpringBootTest
public class ClubMemberTests {
  @Autowired
  private ClubMemberRepository repository;
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Test
  public void testExist(){
    Assertions.assertNotNull(repository);
  }

  @Test
  public void insertDummies(){
    //1 ~ 80 user 지정
    // 81 ~ 90 user, manager
    // user, manager, admin
    IntStream.rangeClosed(1, 100).forEach(i -> {
      ClubMember member = ClubMember.builder()
              .email("user"+i+"@gmail.com")
              .name("사용자" + i)
              .fromSocial(false)
              .password(passwordEncoder.encode("1111"))
              .build();
      //default role
      member.addMemberRole(ClubMemberRole.USER);
      if(i > 80) {
        member.addMemberRole(ClubMemberRole.MANAGER);
      }
      if(i > 90){
        member.addMemberRole(ClubMemberRole.ADMIN);
      }
      repository.save(member);
    });
  }
}
