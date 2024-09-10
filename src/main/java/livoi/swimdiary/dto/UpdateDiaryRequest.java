package livoi.swimdiary.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 *  감정일기 수정을 위한 DTO
 *  감정 일기 수정시 필요한 항목들을 MoodDiary Entity와 매핑하는 DTO
 *  저장한 감정일기를 수정해 서버에 등록하고자 할때 사용합니다.
 */

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDiaryRequest {

  private Long diaryId;
  private Long userId;

  @NotNull
  private String workoutMood;
  private String workoutType;
  @Positive
  private Integer workoutCount;
  private String workoutLog;
  private LocalDate modifiedAt;
}
