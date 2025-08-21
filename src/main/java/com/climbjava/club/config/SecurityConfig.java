package com.climbjava.club.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Log4j2
public class SecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public InMemoryUserDetailsManager userDetailService() {
    UserDetails user = User.builder()
            .username("user1")
            .password(passwordEncoder().encode("1111"))
            .roles("USER")
            .build();
    return new InMemoryUserDetailsManager(user);
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(c -> c.disable())
            .authorizeHttpRequests((auth) -> {
      auth.requestMatchers("sample/all").permitAll() // 모든 사람 진입 가능
              .requestMatchers("sample/admin").hasRole("ADMIN") // Admin만 진입 가능
              .requestMatchers("sample/member").hasAnyRole("ADMIN", "USER")
              .requestMatchers(".well-known/**").permitAll()
              .anyRequest().authenticated(); // 설정한 경로 외 모든지점에서 로그인 한 사람만 허용
    });
    http.formLogin(Customizer.withDefaults());
    http.logout(Customizer.withDefaults());
    return http.build();
  }
}
