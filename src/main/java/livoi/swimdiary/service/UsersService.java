package livoi.swimdiary.service;

import livoi.swimdiary.domain.Users;
import livoi.swimdiary.dto.AddUserRequest;
import livoi.swimdiary.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 사용자 기능을 위한 서비스 로직
 */

@RequiredArgsConstructor
@Service
public class UsersService {

  private final UsersRepository usersRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  /**
   * 회원정보를 저장하는 서비스 로직입니다.
   * @param dto 에서 저장할 사용자가 입력한 정보를 가지고 옵니다.
   * @return 저장된 사용자의 정보를 반환합니다.
   */
  public Long save(AddUserRequest dto) {
    return usersRepository.save(Users.builder()
        .username(dto.getUsername())
        .email(dto.getEmail())
        .password(bCryptPasswordEncoder.encode(dto.getPassword()))
        .nickname(dto.getNickname())
        .swimRoutine(dto.getSwimRoutine())
        .build()).getUserId();
  }
}
