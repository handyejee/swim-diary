package livoi.swimdiary.dto;

import livoi.swimdiary.domain.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 사용자 등록을 위한 DTO
 * 새로운 사용자를 서버에 등록할때 사용합니다.
 */

@Builder
@Getter
@Setter
public class AddUserRequest {

  private Long userId;
  private String username;
  private String email;
  private String password;
  private String nickname;
  private Integer swimRoutine;

}
