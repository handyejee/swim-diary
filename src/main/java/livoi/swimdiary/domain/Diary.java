package livoi.swimdiary.domain;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 감정일기 항목을 나타내는 클래스
 */

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Diary {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "diary_id", updatable = false)
  private Long diaryId;

  @ManyToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "user_id", nullable = false) // FK 지정
  private Users users;

  @Column(name = "workout_mood", nullable = false)
  private String workoutMood;

  @Column(name = "workout_type")
  private String workoutType;

  @Column(name = "workout_count")
  private Integer workoutCount;

  @Column(name = "workout_log")
  private String workoutLog;

  @Column(name = "created_at")
  private LocalDate createdAt;

  @Column(name = "modified_at")
  private LocalDate modifiedAt;

  @Column(name = "deleted_at")
  private LocalDate deletedAt;

  @Builder
  public Diary(Users users, String workoutMood, String workoutType, Integer workoutCount, String workoutLog, LocalDate createdAt, LocalDate modifiedAt, LocalDate deletedAt){
    this.users = users;
    this.workoutMood = workoutMood;
    this.workoutType = workoutType;
    this.workoutCount = workoutCount;
    this.workoutLog = workoutLog;
    this.createdAt = createdAt;
    this.modifiedAt = modifiedAt;
    this.deletedAt = deletedAt;
  }

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDate.now(); // 현재 날짜로 설정
    this.modifiedAt = LocalDate.now(); // 생성 시 수정일시도 동일하게 설정
  }

  @PreUpdate
  protected void onUpdate() {
    this.modifiedAt = LocalDate.now();
  }

  public void markAsDeleted() {
    this.deletedAt = LocalDate.now();
  }
}
