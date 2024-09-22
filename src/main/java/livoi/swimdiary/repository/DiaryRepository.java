package livoi.swimdiary.repository;

import java.time.LocalDate;
import java.util.List;
import livoi.swimdiary.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 사용자 엔티티 {@link Diary} 에 대한 데이터베이스 상호작용을 처리하는 인터페이스 입니다.
 */

public interface DiaryRepository extends JpaRepository<Diary, Long> {

  List<Diary> findAllByCreatedAtBetween(LocalDate startDate, LocalDate endDate);
 }
