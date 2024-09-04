package livoi.swimdiary.dto;

import java.time.LocalDate;
import livoi.swimdiary.domain.Diary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class DiaryResponseDto {

  private final Long diaryId;
  private final Long userId;
  private final String workoutMood;
  private final String workoutType;
  private final Integer workoutCount;
  private final String workoutLog;
  private final LocalDate createdAt;
  private final LocalDate modifiedAt;
  private final LocalDate deletedAt;

  public static DiaryResponseDto fromEntity(Diary diary) {
    return DiaryResponseDto.builder()
        .diaryId(diary.getDiaryId())
        .userId(diary.getUsers().getUserId())
        .workoutMood(diary.getWorkoutMood())
        .workoutType(diary.getWorkoutType())
        .workoutCount(diary.getWorkoutCount())
        .workoutLog(diary.getWorkoutLog())
        .createdAt(diary.getCreatedAt())
        .modifiedAt(diary.getModifiedAt())
        .deletedAt(diary.getDeletedAt())
        .build();
  }
}
