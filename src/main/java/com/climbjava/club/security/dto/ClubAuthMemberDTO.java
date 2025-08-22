package com.climbjava.club.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Log4j2
@Getter
@Setter
@ToString
public class ClubAuthMemberDTO extends User implements OAuth2User {

  private String email;

  private String password;

  private String name;

  private boolean fromSocial;

  private Map<String, Object> attributes;

  public ClubAuthMemberDTO(String username, String password, boolean fromSocial, Collection<GrantedAuthority> authorities, Map<String, Object> attributes) {
    this(username, password, fromSocial, authorities);
    this.attributes = attributes;
  }

  public ClubAuthMemberDTO(String username, String password, boolean fromSocial, Collection<? extends GrantedAuthority> authorities) {
    super(username, password, authorities);
    this.email = username;
    this.password = password;
    this.fromSocial = fromSocial;
  }


}
