package livoi.swimdiary.controller;

import jakarta.validation.Valid;
import livoi.swimdiary.domain.Users;
import livoi.swimdiary.dto.AddUserRequest;
import livoi.swimdiary.dto.AddUserResponseDto;
import livoi.swimdiary.dto.LoginUserRequest;
import livoi.swimdiary.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 사용자를 등록, 로그인하고 삭제하기 위한 API Controller
 */
@RequiredArgsConstructor
@RestController
public class UserApiController {

  private final UsersService usersService;

  @PostMapping("/signup")
  public ResponseEntity<AddUserResponseDto> signup(@RequestBody @Valid AddUserRequest request){
    Users savedUsers = usersService.saveUser(request);
    AddUserResponseDto userResponseDto = AddUserResponseDto.fromEntity(savedUsers);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(userResponseDto);
  }

  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestBody @Valid LoginUserRequest loginUserRequest){
    String token = usersService.signIn(loginUserRequest);

    return ResponseEntity.ok(token);
  }

  @DeleteMapping("user/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable long userId) {
    usersService.deleteUser(userId);

    return ResponseEntity.ok()
        .build();
  }

}
