package com.climbjava.club.security.service;

import com.climbjava.club.entity.ClubMember;
import com.climbjava.club.entity.ClubMemberRole;
import com.climbjava.club.repository.ClubMemberRepository;
import com.climbjava.club.security.dto.ClubAuthMemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ClubOAuthUserDetailsService extends DefaultOAuth2UserService {

  private final ClubMemberRepository repository;

  private final PasswordEncoder passwordEncoder;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    log.info("======여기는 ClubOAuthUserDetailsService 영역입니다 ======");
    log.info("userRequest={}", userRequest);

    String clientName = userRequest.getClientRegistration().getClientName();
    log.info("clientName={}", clientName);

    log.info(userRequest.getAdditionalParameters());

    OAuth2User oAuth2User = super.loadUser(userRequest);

    log.info("oAuth2User={}", oAuth2User);

    oAuth2User.getAttributes().forEach((k, v) -> {
      log.info("k={}, v={}", k, v);
    });

    String email = null;

    if(clientName.equals("Google")) {
      email = oAuth2User.getAttribute("email");
    }
    log.info("email={}", email);

//    ClubMember clubMember = saveSocialMember(email);

    //return oAuth2User;
    ClubMember clubMember = saveSocialMember(email);
    ClubAuthMemberDTO clubAuthMemberDTO = new ClubAuthMemberDTO(
            clubMember.getEmail(),
            clubMember.getPassword(),
            true,
            clubMember.getRoleSet().stream()
                    .map( role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                    .collect(Collectors.toList()),
            oAuth2User.getAttributes()
    );

    return clubAuthMemberDTO;

  }

  private ClubMember saveSocialMember(String email) {
    Optional<ClubMember> result = repository.findByEmail(email, true);

    if(result.isPresent()) {
      return result.get();
    }

    //없다면 회원 추가 패스워드는 1111 이름은 그냥 이메일 주소로

    ClubMember clubMember = ClubMember.builder()
            .email(email)
            .name(email)
            .password(passwordEncoder.encode("1111"))
            .fromSocial(true)
            .build();
    clubMember.addMemberRole(ClubMemberRole.USER);

    repository.save(clubMember);

    return clubMember;
  }

}
