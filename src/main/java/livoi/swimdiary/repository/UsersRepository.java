package livoi.swimdiary.repository;

import livoi.swimdiary.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {

}
