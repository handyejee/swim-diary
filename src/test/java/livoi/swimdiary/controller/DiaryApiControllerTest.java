package livoi.swimdiary.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import livoi.swimdiary.domain.Diary;
import livoi.swimdiary.domain.Users;
import livoi.swimdiary.dto.AddDiaryRequest;
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

  @BeforeEach
  public void mockMvcSetup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .build();
    diaryRepository.deleteAll();
  }

  @DisplayName("addMoodDiary : 감정일기 등록에 성공한다")
  @Test
  public void addMindDiary() throws Exception {

    //given
    final String url = "/mind-diary";

    // 일기 작성에 필요한 요청 객체 만들기 : Mock Users 객체 생성
    final Users users = Users.builder()
        .username("testuser")
        .email("test@gmail.com")
        .password("pw123@!!")
        .nickname("testyy")
        .build();

    Users savedUser = usersRepository.save(users);

    final String mood = "PROUD";

    final AddDiaryRequest addDiaryRequest = AddDiaryRequest.builder()
        .userId(users.getUserId())
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
    result.andExpect(status().isCreated());

    List<Diary> mindDiaries = diaryRepository.findAll();

    assertThat(mindDiaries.size()).isEqualTo(1);
    assertThat(mindDiaries.get(0).getWorkoutMood()).isEqualTo(mood);
    assertThat(mindDiaries.get(0).getUsers().getUserId()).isEqualTo(savedUser.getUserId());

  }
}