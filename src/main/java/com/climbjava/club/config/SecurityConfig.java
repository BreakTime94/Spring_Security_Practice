package com.climbjava.club.config;

import com.climbjava.club.security.filter.ApiCheckFilter;
import com.climbjava.club.security.filter.ApiLoginFilter;
import com.climbjava.club.security.handler.ClubLoginSuccessHandler;
import com.climbjava.club.security.util.JWTUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Log4j2
public class SecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public ClubLoginSuccessHandler clubLoginSuccessHandler() {
    return new ClubLoginSuccessHandler(passwordEncoder());
  }

  @Bean
  public ApiCheckFilter apiCheckFilter() {
    return new ApiCheckFilter("/notes/**/*", jwtUtil());
  }

  @Bean
  public JWTUtil jwtUtil() {return new JWTUtil();}

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
    //ApiLoginFilter 등록 사전준비
    AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
    AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

    http.authenticationManager(authenticationManager);

    //ApiLoginFilter 등록
    ApiLoginFilter apiLoginFilter = new ApiLoginFilter("/api/login", jwtUtil());
    apiLoginFilter.setAuthenticationManager(authenticationManager);

    http.csrf(c -> c.disable())
            .authorizeHttpRequests((auth) -> {
              auth.requestMatchers("sample/all", "/notes/**", "/api/login/**").permitAll() // 모든 사람 진입 가능
              .requestMatchers("sample/member").hasRole("USER")
              .requestMatchers("sample/admin").hasRole("ADMIN") // Admin만 진입 가능
              .requestMatchers("/member/modify", "/member/modify/**").hasRole("USER")
              .requestMatchers("/error").permitAll();

    })
            .formLogin(form -> form.defaultSuccessUrl("/sample/all", false))
            .logout(Customizer.withDefaults());

            http.oauth2Login(form -> form.defaultSuccessUrl("/sample/all", false).successHandler(clubLoginSuccessHandler()));

            http.rememberMe(form -> form.tokenValiditySeconds(60));
            //Filter의 순서 바꾸기?
            http.addFilterBefore(apiCheckFilter(), UsernamePasswordAuthenticationFilter.class);



    http.addFilterBefore(apiLoginFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
