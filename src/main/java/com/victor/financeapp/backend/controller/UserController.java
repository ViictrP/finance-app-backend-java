package com.victor.financeapp.backend.controller;

import com.victor.financeapp.backend.service.UserService;
import com.victor.financeapp.backend.service.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
//@Secured({ROLE_USER, ROLE_ADMIN})
public class UserController {

  private final UserService service;

  @GetMapping("/me")
  public ResponseEntity<UserDTO> getProfile(ProfileCriteria criteria) {
    log.info("Get profile request received");
    return ResponseEntity.ok(service.getUser(criteria));
  }

  @GetMapping("/balance")
  public ResponseEntity<UserBalanceDTO> getBalance(ProfileCriteria criteria) {
    log.info("Balance request received");
    var balance = service.getUserBalance(criteria);
    return ResponseEntity.ok().body(balance);
  }

  @PostMapping
  public ResponseEntity<UserDTO> createProfile(@Valid @RequestBody ProfileDTO profile) {
    log.info("Profile creation request received {}", profile.getName());
    var newProfile = service.create(profile);
    return ResponseEntity.ok().body(newProfile);
  }

  @PutMapping
  public ResponseEntity<UserDTO> createProfile(@Valid @RequestBody UpdateProfileDTO profile) {
    log.info("Profile creation request received");
    var updatedProfile = service.update(profile);
    return ResponseEntity.ok().body(updatedProfile);
  }
}
