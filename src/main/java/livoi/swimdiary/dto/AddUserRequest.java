package livoi.swimdiary.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import livoi.swimdiary.domain.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 사용자 등록을 위한 DTO
 * 새로운 사용자를 서버에 등록할때 사용합니다.
 */

@Builder
@Getter
@Setter
public class AddUserRequest {

  private Long userId;
  @NotNull
  private String username;
  @NotNull
  private String email;
  @NotNull
  private String password;
  private String nickname;
  @Positive
  private Integer swimRoutine;

  public Users toEntity(BCryptPasswordEncoder encoder) {
    return Users.builder()
        .username(username)
        .email(email)
        .password(encoder.encode(password))
        .nickname(nickname)
        .swimRoutine(swimRoutine)
        .build();
  }
}
