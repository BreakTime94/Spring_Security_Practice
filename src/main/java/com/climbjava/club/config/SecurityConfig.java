package com.climbjava.club.config;

import com.climbjava.club.security.handler.ClubLoginSuccessHandler;
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

//  @Bean
//  public InMemoryUserDetailsManager userDetailService() {
//    UserDetails user = User.builder()
//            .username("user1")
//            .password(passwordEncoder().encode("1111"))
//            .roles("USER")
//            .build();
//    return new InMemoryUserDetailsManager(user);
//  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(c -> c.disable())
            .authorizeHttpRequests((auth) -> {
              auth.requestMatchers("sample/all").permitAll() // 모든 사람 진입 가능
              .requestMatchers("sample/member").hasRole("USER")
              .requestMatchers("sample/admin").hasRole("ADMIN") // Admin만 진입 가능
              .requestMatchers("/member/modify", "/member/modify/**").hasRole("USER")
              .requestMatchers("/error").permitAll();

    })
            .formLogin(form -> form.defaultSuccessUrl("/sample/all", false))
            .logout(Customizer.withDefaults());

            http.oauth2Login(form -> form.defaultSuccessUrl("/sample/all", false).successHandler(clubLoginSuccessHandler()));

            http.rememberMe(form -> form.tokenValiditySeconds(60 * 60 * 24 * 7));
    return http.build();
  }

  @Bean
  public ClubLoginSuccessHandler clubLoginSuccessHandler() {
    return new ClubLoginSuccessHandler(passwordEncoder());
  }
}
