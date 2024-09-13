package livoi.swimdiary.service;

import livoi.swimdiary.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * 사용자 정보를 조회하기 위한 서비스 로직
 * UserDetailsService 인터페이스를 구현합니다.
 */
@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {

  private final UsersRepository usersRepository;

  /**
   * 이메일로 사용자를 검색합니다.
   * @param email 사용자의 이메일
   * @return userRepository에서 검색한 이메일 반환.
   * @throws IllegalArgumentException 사용자 이메일 찾을 수 없을 경우 예외 발생
   */
  @Override
  public UserDetails loadUserByUsername(String email){
    return usersRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("User email not found" + email));
  }
}
