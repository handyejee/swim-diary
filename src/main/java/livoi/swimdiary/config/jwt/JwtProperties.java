package livoi.swimdiary.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * issuer, secretkey를 property에서 가져와서 사용하기 위한 클래스
 */
@Getter
@Setter
@Component
@ConfigurationProperties("jwt")
public class JwtProperties {

  private String issuer;
  private String secretKey;

}
