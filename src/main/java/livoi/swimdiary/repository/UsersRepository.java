package livoi.swimdiary.repository;

import java.util.Optional;
import livoi.swimdiary.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 사용자 엔티티 {@link Users} 에 대한 데이터베이스 상호작용을 처리하는 인터페이스 입니다.
 * 이 인터페이스는 {@code JpaRepository}를 확장해 Spring Data Jpa의 기능을 사용합니다.
 */
public interface UsersRepository extends JpaRepository<Users, Long> {

  /**
   * 이메일로 사용자를 검색합니다.
   * @param email 사용자의 이메일
   * @return 이메일과 일치하는 사용자가 존재할 경우 Users 객체 반환
   */
  Optional<Users> findByEmail(String email);
}
