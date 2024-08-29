package livoi.swimdiary.dto;

import java.time.LocalDate;
import livoi.swimdiary.domain.Diary;
import livoi.swimdiary.domain.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 *  감정일기 DTO
 *  감정 일기 등록시 필요한 항목들을 MoodDiary Entity와 매핑하는 DTO
 *  새로운 일기 항목을 서버에 등록할때 사용합니다.
 */

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddDiaryRequest {

  private Long diaryId;
  private Long userId;
  private String workoutMood;
  private String workoutType;
  private Integer workoutCount;
  private String workoutLog;
  private LocalDate createdAt;
  private LocalDate modifiedAt;
  private LocalDate deletedAt;

  public Diary toEntity(Users users){
    return Diary.builder()
        .users(users)
        .workoutMood(workoutMood)
        .workoutType(workoutType)
        .workoutCount(workoutCount)
        .workoutLog(workoutLog)
        .createdAt(createdAt)
        .modifiedAt(modifiedAt)
        .deletedAt(deletedAt)
        .build();
  }
}
