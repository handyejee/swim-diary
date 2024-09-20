package livoi.swimdiary.dto;

import livoi.swimdiary.domain.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 사용자 등록시 응답으로 전달되는 ResponseDto
 */
@Getter
@AllArgsConstructor
@Builder
public class AddUserResponseDto {

  private final Long userId; // DB에서 자동으로 생성되는 값
  private final String username;
  private final String email;
  private final String nickname;
  private final Integer swimRoutine;

  public static AddUserResponseDto fromEntity(Users users){
    return AddUserResponseDto.builder()
        .userId(users.getUserId())
        .username(users.getUsername())
        .email(users.getEmail())
        .nickname(users.getNickname())
        .swimRoutine(users.getSwimRoutine())
        .build();
  }

}
