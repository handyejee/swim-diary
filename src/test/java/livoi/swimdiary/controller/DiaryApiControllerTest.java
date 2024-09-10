package livoi.swimdiary.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import java.util.List;
import livoi.swimdiary.domain.Diary;
import livoi.swimdiary.domain.Users;
import livoi.swimdiary.dto.AddDiaryRequest;
import livoi.swimdiary.dto.UpdateDiaryRequest;
import livoi.swimdiary.repository.DiaryRepository;
import livoi.swimdiary.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
class DiaryApiControllerTest {

  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected ObjectMapper objectMapper;

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private DiaryRepository diaryRepository;

  @Autowired
  private UsersRepository usersRepository;

  private Users savedUser;

  @BeforeEach
  public void mockMvcSetup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .build();
    diaryRepository.deleteAll();
    usersRepository.deleteAll();

    // 공통으로 사용할 Users 객체 생성, 저장
    savedUser = Users.builder()
        .username("testuser")
        .email("test@gmail.com")
        .password("pw123@!!")
        .nickname("testyy")
        .build();

    savedUser = usersRepository.save(savedUser);
  }

  @DisplayName("addDiary : 감정일기 등록에 성공한다")
  @Test
  public void addMindDiary() throws Exception {

    //given
    final String url = "/mind-diary";

    final String mood = "PROUD";

    final AddDiaryRequest addDiaryRequest = AddDiaryRequest.builder()
        .userId(savedUser.getUserId())
        .workoutMood(mood)
        .build();

    final String requestBody = objectMapper.writeValueAsString(addDiaryRequest);

    //감정일기 추가 API에 요청을 보낸다.
    //when
    ResultActions result = mockMvc.perform(post(url)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(requestBody)
    );

    // 응답코드를 확인한다
    //then
    result.andExpect(status().isCreated())
        .andDo(print());

    List<Diary> mindDiaries = diaryRepository.findAll();

    assertThat(mindDiaries.size()).isEqualTo(1);
    assertThat(mindDiaries.get(0).getWorkoutMood()).isEqualTo(mood);
    assertThat(mindDiaries.get(0).getUsers().getUserId()).isEqualTo(savedUser.getUserId());
  }

  @DisplayName("updateArticle: 감정일기 수정에 성공한다.")
  @Test
  public void updateArticle() throws Exception {

    //given
    final String url = "/mind-diary/{diaryId}";

    // 일기 생성
    final String mood = "PROUD";

    final AddDiaryRequest addDiaryRequest = AddDiaryRequest.builder()
        .userId(savedUser.getUserId())
        .workoutMood(mood)
        .build();

    // serialize
    final String requestBody = objectMapper.writeValueAsString(addDiaryRequest);

    // 일기 생성 요청
    ResultActions savedDiaryResultActions = mockMvc.perform(post("/mind-diary")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody));

    savedDiaryResultActions
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.diaryId").exists());

    // ---수정 로직---
    // diaryId 추출
    Integer diaryIdInt = JsonPath.read(
        savedDiaryResultActions.andReturn().getResponse().getContentAsString(), "$.diaryId");
    Long savedDiaryId = diaryIdInt.longValue(); // Integer 값을 Long으로 변환

    // 수정 항목
    final String updatedMood = "ENERGIZED";
    final String updatedWorkoutLog = "I have enjoyed my swimming lesson";

    UpdateDiaryRequest request = UpdateDiaryRequest.builder()
        .workoutMood(updatedMood)
        .workoutLog(updatedWorkoutLog)
        .build();

    final String updateRequestBody = objectMapper.writeValueAsString(request);

    //when
    ResultActions result = mockMvc.perform(put(url, savedDiaryId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(updateRequestBody))
        .andDo(print());

    //then
    result.andExpect(status().isOk());
    Diary updatedDiary = diaryRepository.findById(savedDiaryId)
        .orElseThrow(() -> new IllegalArgumentException("UserId not found"));

    assertThat(updatedDiary.getWorkoutMood()).isEqualTo(updatedMood);
    assertThat(updatedDiary.getWorkoutLog()).isEqualTo(updatedWorkoutLog);
  }

  @DisplayName("Delete Diary : 감정일기 삭제에 성공한다.")
  @Test
  void deleteDiary() throws Exception {
    //given
    final String url = "/mind-diary/{diaryId}";

    final String mood = "PROUD";

    final AddDiaryRequest addDiaryRequest = AddDiaryRequest.builder()
        .userId(savedUser.getUserId())
        .workoutMood(mood)
        .build();

    Diary savedDiary = diaryRepository.save(addDiaryRequest.toEntity(savedUser));

    //when
    mockMvc.perform(delete(url, savedDiary.getDiaryId()))
        .andExpect(status().isOk())
        .andDo(print());

    //then
    List<Diary> diaries = diaryRepository.findAll();

    assertThat(diaries).isEmpty();
  }
}