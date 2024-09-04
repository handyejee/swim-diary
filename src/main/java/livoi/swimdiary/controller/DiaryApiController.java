package livoi.swimdiary.controller;

import livoi.swimdiary.domain.Diary;
import livoi.swimdiary.dto.AddDiaryRequest;
import livoi.swimdiary.dto.DiaryResponseDto;
import livoi.swimdiary.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class DiaryApiController {

  private final DiaryService diaryService;

  @PostMapping("/mind-diary")
  public ResponseEntity<DiaryResponseDto> addMoodDiary(@RequestBody @Validated AddDiaryRequest request){
    Diary savedDiary = diaryService.save(request);
    DiaryResponseDto responseDto = DiaryResponseDto.fromEntity(savedDiary);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(responseDto);
  }

}
