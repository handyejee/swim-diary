package livoi.swimdiary.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import livoi.swimdiary.domain.Diary;
import livoi.swimdiary.domain.Users;
import livoi.swimdiary.dto.AddDiaryRequest;
import livoi.swimdiary.dto.GetDiaryResponseDto;
import livoi.swimdiary.dto.UpdateDiaryRequest;
import livoi.swimdiary.dto.UpdateDiaryResponseDto;
import livoi.swimdiary.repository.DiaryRepository;
import livoi.swimdiary.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 감정일기 기능을 위한 서비스 로직
 * 감정일기를 등록, 수정, 삭제하고 조회합니다.
 */

@Slf4j
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
   * @throws IllegalArgumentException 만약 사용자id 항목을 찾지 못하면 User not found 예외를 발생시킵니다.
   */
  public Diary save(AddDiaryRequest request) {
    Users users = usersRepository.findById(request.getUserId())
        .orElseThrow(() -> new IllegalArgumentException("User not found : " + request.getUserId()));

    Diary diary = request.toEntity(users);
    return diaryRepository.save(diary);
  }

  /**
   * 감정일기를 수정해 저장합니다.
   *
   * @param diaryId 각 감정일기의 고유한 diaryId 값을 받아옵니다.
   * @param request 감정일기를 수정할 값(운동 감정, 영법 종류, 운동횟수) 항목을 받아옵니다.
   * @return 수정된 감정일기 entity를 반환합니다.
   * @throws IllegalArgumentException 만약 diaryId 항목을 찾지 못하면 diaryId not found 예외를 발생시킵니다.
   */
  @Transactional
  public UpdateDiaryResponseDto update(long diaryId, UpdateDiaryRequest request) {
    Diary diary = diaryRepository.findById(diaryId)
        .orElseThrow(() -> new IllegalArgumentException("diaryId not found : " + diaryId));

    diary.update(request.getWorkoutMood(), request.getWorkoutType(), request.getWorkoutCount(),
        request.getWorkoutLog(), request.getModifiedAt());

    return UpdateDiaryResponseDto.fromEntity(diary);
  }

  /**
   * 오늘 기준으로 등록된 감정일기를 조회합니다.
   *
   * @return 감정일기 목록을 조회한 Diary 엔티티의 GetDiaryResponseDto를 리스트로 변환해 반환
   */
  public Page<GetDiaryResponseDto> getDiaryOfToday(Pageable pageable){
    LocalDate today = LocalDate.now();
    Page<Diary> diaryOfToday = diaryRepository.findAllByCreatedAt(today, pageable);

    return diaryOfToday.map(GetDiaryResponseDto::fromEntity);
  }

  /**
   * 사용자가 선택한 날짜 기준으로 감정일기를 조회합니다.
   *
   * @param startDate 검색 시작 일자
   * @param endDate 검색 종료 일자
   * @return 감정일기 목록을 조회한 Diary 엔티티의 GetDiaryResponseDto를 리스트로 변환해 반환
   */
  public Page<GetDiaryResponseDto> getDiaryByDate(LocalDate startDate, LocalDate endDate, Pageable pageable) {
    if (startDate.isAfter(endDate)) {
      throw new IllegalArgumentException("Start date cannot be after end date.");
    }
    Page<Diary> diaries = diaryRepository.findAllByCreatedAtBetween(startDate, endDate, pageable);

    return diaries.map(GetDiaryResponseDto::fromEntity);
  }

  /**
   * 사용자가 조회한 연도와 월에 작성한 감정일기를 조회합니다.
   *
   * @param yearMonth 사용자가 조회한 연도-월
   * @param pageable 페이지 정보
   * @return 연도와 월 기반으로 조회한 값을 페이지별로
   */
  public Page<GetDiaryResponseDto> getDiaryByMonth(YearMonth yearMonth, Pageable pageable){
    YearMonth twoYearsAgo = YearMonth.now().minus(2, ChronoUnit.YEARS);
    YearMonth currentYearMonth = YearMonth.now();

    log.info(("currentYearMonth {}"), currentYearMonth);
    log.info("twoYearsAgo {}", twoYearsAgo);

    if (yearMonth.isBefore(twoYearsAgo)|| yearMonth.isAfter(currentYearMonth)) {
      throw new IllegalArgumentException("The range that can be viewed is from 2 years ago to the present.");
    }

    LocalDate startDate = yearMonth.atDay(1); // 입력 받은 월의 첫번째 날
    LocalDate endDate = yearMonth.atEndOfMonth(); // 입력 받은 월의 마지막 날

    Page<Diary> diaryOfMonth = diaryRepository.findAllByCreatedAtBetween(startDate, endDate, pageable);

    return diaryOfMonth.map(GetDiaryResponseDto::fromEntity);
  }

/**
 * 감정일기를 삭제합니다.
 *
 * @param diaryId 감정일기의 diaryId 값을 받아옵니다.
 */
public void delete(long diaryId) {
  diaryRepository.deleteById(diaryId);
}

}
