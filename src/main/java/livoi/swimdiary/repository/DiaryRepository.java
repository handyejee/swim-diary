package livoi.swimdiary.repository;

import livoi.swimdiary.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

 }
