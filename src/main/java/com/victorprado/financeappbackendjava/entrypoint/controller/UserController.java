package com.victorprado.financeappbackendjava.entrypoint.controller;

import com.victorprado.financeappbackendjava.service.UserService;
import com.victorprado.financeappbackendjava.service.dto.SalaryDTO;
import com.victorprado.financeappbackendjava.service.dto.UserDTO;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {

  private final UserService service;

  @GetMapping("/me")
  @Secured({"ROLE_USER"})
  public ResponseEntity<UserDTO> getProfile(Authentication authentication) {
    log.info("Get profile request received");
    return ResponseEntity.ok(service.getUser(authentication));
  }

  @PostMapping("/salaries")
  @Secured({"ROLE_USER"})
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<SalaryDTO> createSalary(@Valid @RequestBody SalaryDTO salary,
    Authentication authentication) {
    log.info("Create salary request received");
    salary.setUserId(authentication.getName());
    var result = service.createSalary(salary);
    return ResponseEntity.created(URI.create("/v1/users/salaries" + result.getId()))
      .body(result);
  }
}
