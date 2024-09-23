package livoi.swimdiary.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import livoi.swimdiary.domain.Diary;
import livoi.swimdiary.dto.AddDiaryRequest;
import livoi.swimdiary.dto.AddDiaryResponseDto;
import livoi.swimdiary.dto.GetDiaryResponseDto;
import livoi.swimdiary.dto.UpdateDiaryRequest;
import livoi.swimdiary.dto.UpdateDiaryResponseDto;
import livoi.swimdiary.service.DiaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/mind-diary")
public class DiaryApiController {

  private final DiaryService diaryService;

  @PostMapping
  public ResponseEntity<AddDiaryResponseDto> addDiary(@RequestBody @Valid AddDiaryRequest request){
    Diary savedDiary = diaryService.save(request);
    AddDiaryResponseDto responseDto = AddDiaryResponseDto.fromEntity(savedDiary);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(responseDto);
  }

  @PutMapping("/{diaryId}")
  public ResponseEntity<UpdateDiaryResponseDto> updateDiary(@PathVariable long diaryId,
      @RequestBody @Valid UpdateDiaryRequest request) {

    UpdateDiaryResponseDto updatedDiary = diaryService.update(diaryId, request);

    return ResponseEntity.ok().body(updatedDiary);
  }

  @GetMapping("/today")
  public ResponseEntity<List<GetDiaryResponseDto>> getDiaryOfToday() {
    List<GetDiaryResponseDto> diaries = diaryService.getDiaryOfToday();

    return ResponseEntity.ok(diaries);
  }

  @GetMapping("/date")
  public ResponseEntity<List<GetDiaryResponseDto>> getDiaryByDate(
      @RequestParam @NotNull @DateTimeFormat(iso = ISO.DATE) LocalDate startDate,
      @RequestParam @NotNull @DateTimeFormat(iso = ISO.DATE) LocalDate endDate
  ) {
    List<GetDiaryResponseDto> diaries = diaryService.getDiaryByDate(startDate, endDate);

    return ResponseEntity.ok(diaries);
  }

  @GetMapping("/month")
  public ResponseEntity<List<GetDiaryResponseDto>> getDiaryByMonth(
      @RequestParam @NotNull @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth
  ){
    List<GetDiaryResponseDto> diaryOfMonth = diaryService.getDiaryByMonth(yearMonth);

    return ResponseEntity.ok(diaryOfMonth);
  }

  @DeleteMapping("/{diaryId}")
  public ResponseEntity<Void> deleteDiary(@PathVariable long diaryId) {
    diaryService.delete(diaryId);

    return ResponseEntity.ok()
        .build();
  }
}
