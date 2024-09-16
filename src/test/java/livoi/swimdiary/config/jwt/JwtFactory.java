package livoi.swimdiary.config.jwt;


import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import lombok.Builder;
import lombok.Getter;

@Getter
public class JwtFactory {
  private JwtProperties jwtProperties;
  private SecretKey secretKey;

  @PostConstruct
  protected void init() {
    // jwtProperties가 주입된 후에 secretKey를 초기화

    if (jwtProperties == null) {
      throw new IllegalStateException("JwtProperties not injected!");
    }

    this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
  }

  private String subject = "test@gmail.com";
  private Date issuedAt = new Date();
  private Date expiration = new Date(new Date().getTime() + Duration.ofDays(14).toMillis());
  private Map<String, Object> claims = Collections.emptyMap();

  @Builder
  public JwtFactory(String subject, Date issuedAt, Date expiration, Map<String, Object> claims){

    this.subject = subject != null ? subject : this.subject;
    this.issuedAt = issuedAt != null? issuedAt : this.issuedAt;
    this.expiration = expiration != null ? expiration : this.expiration;
    this.claims = claims != null ? claims : this.claims;
  }

  public static JwtFactory withDefaultValues(){
    return JwtFactory.builder().build();
  }

  public String createToken(JwtProperties jwtProperties){
    return Jwts.builder()
        .header()
          .type("jwt")
        .and()
        .issuer(jwtProperties.getIssuer())
        .subject(subject)
        .expiration(expiration)
        .claims(claims)
        .signWith(secretKey)
        .compact();
  }
}
