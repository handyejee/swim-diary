package livoi.swimdiary.config;

import livoi.swimdiary.config.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 스프링 시큐리티를 설정하는 역할을 하는 클래스 입니다.
 */
@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {

  private final TokenProvider tokenProvider;

  /**
   * SecurityFilterChain 을 설정하는 메서드
   *
   * @param http Security 설정을 구성하기 위한 객체
   * @return 설정된 SecurityFilterChain 반환
   * @throws Exception 필터 체인 설정 중 오류가 발생할 경우 예외 발생
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(authz -> authz
            .requestMatchers("/login", "/signup").permitAll()
            .anyRequest().authenticated()
        )
        .addFilterBefore(new TokenAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  /**
   * AuthenticationManager를 생성해 인증 절차를 처리하는 메서드
   *
   * @param authenticationConfiguration 인증 설정을 제공하는 객체
   * @return 설정된 AuthenticationManager를 반환
   * @throws Exception 인증 매니저 생성 중 오류 발생시 예외 발생
   */
  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  /**
   * 비밀번호를 인코딩 하는 메서드
   * 사용자 비밀번호를 암호화 해 저장하고, 로그인 시 저장된 값을 비교합니다.
   *
   * @return BCryptPasswordEncoder 객체를 반환
   */
  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
