package livoi.swimdiary.dto;

import java.time.LocalDate;
import livoi.swimdiary.domain.Diary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 수정된 다이어리 항목에 대한 응답 데이터를 나타내는 클래스 입니다.
 */

@Getter
@AllArgsConstructor
@Builder
public class UpdateDiaryResponseDto {

  private final String workoutMood;
  private final String workoutType;
  private final Integer workoutCount;
  private final String workoutLog;
  private final LocalDate modifiedAt;

  /**
   * Diary 엔티티를 UpdateDiaryResponseDto로 변환합니다.
   *
   * @param diary 변환할 Diary 엔티티 입니다.
   * @return Diary 엔티티에서 추출된 데이터를 AddDiaryResponse 객체로 변환한 후 그 객체를 반환합니다.
   */
  public static UpdateDiaryResponseDto fromEntity(Diary diary) {
    return UpdateDiaryResponseDto.builder()
        .workoutMood(diary.getWorkoutMood())
        .workoutType(diary.getWorkoutType())
        .workoutCount(diary.getWorkoutCount())
        .workoutLog(diary.getWorkoutLog())
        .modifiedAt(diary.getModifiedAt())
        .build();
  }


}
