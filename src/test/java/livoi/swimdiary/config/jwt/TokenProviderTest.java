package livoi.swimdiary.config.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import javax.crypto.SecretKey;
import livoi.swimdiary.domain.Users;
import livoi.swimdiary.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TokenProviderTest {

  @Autowired
  private TokenProvider tokenProvider;

  @Autowired
  private UsersRepository usersRepository;

  @Autowired
  private JwtProperties jwtProperties;

  private SecretKey secretKey;

  @BeforeEach
  void setUp() {
    // jwtProperties가 주입된 후에 secretKey를 초기화
    secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
  }

  @DisplayName("generateToken() : 사용자 정보와 만료기간을 전달해 토큰을 만들 수 있다.")
  @Test
  void generateToken() {
    //given
    Users testUser = usersRepository.save(Users.builder()
        .username("김탁구")
        .email("testemail@gmail.com")
        .password("dev1234!@")
        .nickname("takk")
        .swimRoutine(2)
        .build()
    );

    // when
    String token = tokenProvider.generateToken(testUser, Duration.ofDays(14));

    // then
    Long userId = Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .get("userId", Long.class);

    assertThat(userId).isEqualTo(testUser.getUserId());

  }

  @DisplayName("validToken() : 토큰 검증시 유효한 토큰일 경우 유효성 검사에 성공한다.")
  @Test
  void validToken_validToken() {
    // given
    String token = JwtFactory.withDefaultValues().createToken(jwtProperties);

    // when
    boolean result = tokenProvider.validToken(token);

    // then
    assertThat(result).isTrue();

  }
}