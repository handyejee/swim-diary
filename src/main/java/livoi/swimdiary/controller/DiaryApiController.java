package livoi.swimdiary.controller;

import livoi.swimdiary.domain.Diary;
import livoi.swimdiary.dto.AddDiaryRequest;
import livoi.swimdiary.dto.AddDiaryResponseDto;
import livoi.swimdiary.dto.UpdateDiaryRequest;
import livoi.swimdiary.dto.UpdateDiaryResponseDto;
import livoi.swimdiary.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class DiaryApiController {

  private final DiaryService diaryService;

  @PostMapping("/mind-diary")
  public ResponseEntity<AddDiaryResponseDto> addDiary(@RequestBody @Validated AddDiaryRequest request){
    Diary savedDiary = diaryService.save(request);
    AddDiaryResponseDto responseDto = AddDiaryResponseDto.fromEntity(savedDiary);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(responseDto);
  }

  @PutMapping("/mind-diary/{diaryId}")
  public ResponseEntity<UpdateDiaryResponseDto> updateDiary(@PathVariable long diaryId,
      @RequestBody @Validated UpdateDiaryRequest request) {

    UpdateDiaryResponseDto updatedDiary = diaryService.update(diaryId, request);

    return ResponseEntity.ok().body(updatedDiary);
  }

  @DeleteMapping("mind-diary/{diaryId}")
  public ResponseEntity<Void> deleteDiary(@PathVariable long diaryId) {
    diaryService.delete(diaryId);

    return ResponseEntity.ok()
        .build();
  }

}
