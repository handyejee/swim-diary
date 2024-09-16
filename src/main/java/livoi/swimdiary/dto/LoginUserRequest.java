package livoi.swimdiary.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginUserRequest {

  @NotNull
  private String email;
  @NotNull
  private String password;

}
