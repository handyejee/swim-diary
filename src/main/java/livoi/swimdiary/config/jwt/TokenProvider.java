package livoi.swimdiary.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import javax.crypto.SecretKey;
import livoi.swimdiary.domain.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

/**
 * JWT 토큰을 생성하고 검증하기 위한 클래스
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TokenProvider {

  private final JwtProperties jwtProperties;
  private SecretKey secretKey;

  @PostConstruct
  protected void init() {
    // jwtProperties가 주입된 후에 secretKey를 초기화
    this.secretKey = Keys.hmacShaKeyFor(
        jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
  }

  public String generateToken(Users users, Duration expiredAt) {
    Date now = new Date();

    return makeToken(new Date(now.getTime() + expiredAt.toMillis()), users);
  }

  private String makeToken(Date expiry, Users users) {
    Date now = new Date();

    return Jwts.builder()
        .header()
        .type("JWT")
        .and()
        .issuer(jwtProperties.getIssuer())
        .issuedAt(now)
        .expiration(expiry)
        .subject(users.getEmail())
        .claim("userId", users.getUserId())
        .signWith(secretKey, Jwts.SIG.HS512) // 명시하지 않으면 HS256 방식으로 암호화
        .compact();
  }

  public boolean validToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(secretKey)
          .build()
          .parseSignedClaims(token);
      return true;

    } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
      log.info("잘못된 JWT 서명입니다.");
    } catch (ExpiredJwtException e) {
      log.info("만료된 JWT 토큰입니다.");
    } catch (UnsupportedJwtException e) {
      log.info("지원되지 않는 JWT 토큰입니다.");
    } catch (IllegalArgumentException e) {
      log.info("JWT 토큰이 잘못되었습니다.");
    }
    return false;
  }

  public Authentication getAuthentication(String token) {
    Claims claims = getClaims(token);
    Set<SimpleGrantedAuthority> authorities = Collections.singleton(
        new SimpleGrantedAuthority("ROLE_USER"));

    return new UsernamePasswordAuthenticationToken(
        new org.springframework.security.core.userdetails.User(claims.getSubject(),
            "", authorities), token, authorities);
  }

  public Long getUserId(String token) {
    Claims claims = getClaims(token);
    return claims.get("userId", Long.class);
  }

  /**
   * 토큰 기반으로 사용자ID를 가지고 오는 메서드
   *
   * @param token 토큰을 가지고 와
   * @return 토큰을 파싱한 결과를 반환합니다
   */
  private Claims getClaims(String token) {
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }
}
