package com.climbjava.club.security.service;

import com.climbjava.club.entity.ClubMember;
import com.climbjava.club.repository.ClubMemberRepository;
import com.climbjava.club.security.dto.ClubAuthMemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ClubUserDetailService implements UserDetailsService {

  private final ClubMemberRepository repository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.info("ClubUserDetailService.loadUserByUsername" + username);

    Optional<ClubMember> result = repository.findByEmail(username, false);

    if(result.isEmpty()) {
      throw new UsernameNotFoundException("check Email or Social");
    }

    ClubMember clubMember = result.get();

    log.info("============절취선==============");
    log.info(clubMember);

    ClubAuthMemberDTO clubAuthMember = new ClubAuthMemberDTO(
            clubMember.getEmail(), clubMember.getPassword(), clubMember.isFromSocial(), clubMember.getRoleSet().stream().map(role -> new SimpleGrantedAuthority("ROLE_"+role.name())).collect(Collectors.toSet())
    );

    clubAuthMember.setName(clubMember.getName());
    clubAuthMember.setFromSocial(clubMember.isFromSocial());

    return clubAuthMember;
  }
}
