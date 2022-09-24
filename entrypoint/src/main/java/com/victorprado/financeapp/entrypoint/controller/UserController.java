package com.victorprado.financeapp.entrypoint.controller;

import com.victorprado.financeapp.core.usecases.CreateUserUseCase;
import com.victorprado.financeapp.core.usecases.GetUserUseCase;
import com.victorprado.financeapp.entrypoint.mapper.UserEntityResponseMapper;
import com.victorprado.financeapp.entrypoint.request.UserRequest;
import com.victorprado.financeapp.entrypoint.response.UserResponse;
import java.security.Principal;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

  private final CreateUserUseCase createUserUseCase;
  private final GetUserUseCase getUserUseCase;
  private final UserEntityResponseMapper mapper;

  public UserController(
    CreateUserUseCase createUserUseCase,
    GetUserUseCase getUserUseCase,
    UserEntityResponseMapper mapper) {
    this.createUserUseCase = createUserUseCase;
    this.mapper = mapper;
    this.getUserUseCase = getUserUseCase;
  }

  @GetMapping("/me")
  public ResponseEntity<UserResponse> getUserProfile(Principal principal) {
    log.info("get profile request received");
    UserResponse response = mapper.toResponse(getUserUseCase.get(principal.getName()));
    log.info("get profile request finished.");
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<UserResponse> createNewUser(@Valid @RequestBody UserRequest request) {
    log.info("create user request received");
    UserResponse response = mapper.toResponse(
      createUserUseCase.create(mapper.fromRequest(request)));
    log.info("user created");
    return ResponseEntity.ok(response);
  }
}
