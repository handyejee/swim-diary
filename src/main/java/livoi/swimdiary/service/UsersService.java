package livoi.swimdiary.service;

import static java.time.temporal.ChronoUnit.HOURS;

import java.time.Duration;
import livoi.swimdiary.config.jwt.TokenProvider;
import livoi.swimdiary.domain.Users;
import livoi.swimdiary.dto.AddUserRequest;
import livoi.swimdiary.dto.LoginUserRequest;
import livoi.swimdiary.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 사용자 정보를 조회하기 위한 서비스 로직 UserDetailsService 인터페이스를 구현합니다.
 */
@RequiredArgsConstructor
@Service
public class UsersService {

  private final UsersRepository usersRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final TokenProvider tokenProvider;

  /**
   * 사용자를 등록하는 서비스로직
   *
   * @param request User 항목 dto 에서 가져온 사용자 정보
   * @return 등록한 사용자 정보를 반환합니다.
   */
  public Users saveUser(AddUserRequest request) {
    if (usersRepository.existsByEmail(request.getEmail())){
     throw new IllegalArgumentException("User email already exists.");
   }

    Users users = request.toEntity(bCryptPasswordEncoder);
    return usersRepository.save(users);
  }

  /**
   * 사용자의 로그인을 처리하는 서비스 로직
   *
   * @param request 사용자의 로그인 정보 (이메일, 비밀번호)
   * @return 로그인 정보가 일치할 경우 JWT 토큰 반환
   */
  public String signIn(LoginUserRequest request) {

    Users users = usersRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    if (!bCryptPasswordEncoder.matches(request.getPassword(), users.getPassword())) {
      throw new IllegalArgumentException("Invalid password");
    }

    return tokenProvider.generateToken(users, Duration.of(2, HOURS));
  }

  /**
   * 사용자를 삭제하는 서비스 로직
   *
   * @param userId 사용자의 고유번호
   */
  public void deleteUser(long userId){
    usersRepository.deleteById(userId);
  }
}
