package livoi.swimdiary.controller;

import livoi.swimdiary.domain.MindDiary;
import livoi.swimdiary.dto.AddMindDiaryRequest;
import livoi.swimdiary.service.MindDiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class DiaryApiController {

  private final MindDiaryService mindDiaryService;

  @PostMapping("/mind-diary")
  public ResponseEntity<MindDiary> addMoodDiary(@RequestBody AddMindDiaryRequest request){
    MindDiary savedMindDiary = mindDiaryService.save(request);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(savedMindDiary);
  }

}
