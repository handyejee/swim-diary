package livoi.swimdiary.service;

import livoi.swimdiary.domain.Diary;
import livoi.swimdiary.domain.Users;
import livoi.swimdiary.dto.AddDiaryRequest;
import livoi.swimdiary.repository.DiaryRepository;
import livoi.swimdiary.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 감정일기 기능을 위한 서비스 로직
 * 감정일기를 등록, 수정, 삭제하고 조회합니다.
 */

@RequiredArgsConstructor
@Service
public class DiaryService {

  private final DiaryRepository diaryRepository;
  private final UsersRepository usersRepository;

  /**
   * 감정일기 요청을 기반으로 새로운 감정일기를 저장합니다.
   *
   * @param request 저장할 감정일기 항목의 세부정보가 저장됩니다.
   * @return 등록한 감정일기 정보를 반환합니다.
   */
  public Diary save(AddDiaryRequest request) {
    Users users = usersRepository.findById(request.getUserId())
        .orElseThrow(() -> new IllegalArgumentException("User not found : " + request.getUserId()));

    Diary diary = request.toEntity(users);
    return diaryRepository.save(diary);
  }



}
