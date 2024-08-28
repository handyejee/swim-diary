package livoi.swimdiary.dto;

import java.time.LocalDate;
import livoi.swimdiary.domain.MindDiary;
import livoi.swimdiary.domain.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 *  감정일기 DTO
 *  감정 일기 등록시 필요한 항목들을 MoodDiary Entity와 매핑하는 DTO
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class AddMindDiaryRequest {

  private Long diaryId;
  private Users users;
  private String workoutMood;
  private String workoutType;
  private Integer workoutCount;
  private String workoutLog;
  private LocalDate createdAt;
  private LocalDate modifiedAt;
  private LocalDate deletedAt;

  public MindDiary toEntity(){
    return MindDiary.builder()
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
