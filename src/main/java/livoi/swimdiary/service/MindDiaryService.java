package livoi.swimdiary.service;

import livoi.swimdiary.domain.MindDiary;
import livoi.swimdiary.dto.AddMindDiaryRequest;
import livoi.swimdiary.repository.MindDiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 감정일기 기능을 위한 서비스 로직
 * 감정일기를 등록, 수정, 삭제하고 조회합니다.
 */

@RequiredArgsConstructor
@Service
public class MindDiaryService {

  private final MindDiaryRepository mindDiaryRepository;

  public MindDiary save(AddMindDiaryRequest request) {
    return mindDiaryRepository.save(request.toEntity());
  }



}
