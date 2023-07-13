package com.victorprado.financeappbackendjava.entrypoint.controller;

import static com.victorprado.financeappbackendjava.domain.roles.Roles.ROLE_ADMIN;
import static com.victorprado.financeappbackendjava.domain.roles.Roles.ROLE_USER;

import com.victorprado.financeappbackendjava.service.UserService;
import com.victorprado.financeappbackendjava.service.dto.BackupDTO;
import com.victorprado.financeappbackendjava.service.dto.ProfileCriteria;
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
@Secured({ROLE_USER, ROLE_ADMIN})
public class UserController {

  private final UserService service;

  @GetMapping("/me")
  public ResponseEntity<UserDTO> getProfile(
    ProfileCriteria criteria, Authentication authentication) {
    log.info("Get profile request received");
    return ResponseEntity.ok(service.getUser(criteria, authentication));
  }

  @PostMapping("/salaries")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<SalaryDTO> createSalary(@Valid @RequestBody SalaryDTO salary,
    Authentication authentication) {
    log.info("Create salary request received");
    salary.setUserId(authentication.getName());
    var result = service.saveSalary(salary);
    return ResponseEntity.created(URI.create("/v1/users/salaries" + result.getId()))
      .body(result);
  }

  @PostMapping("/backups")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<Void> importBackup(@Valid @RequestBody BackupDTO backup,
    Authentication authentication) {
    log.info("Backup request received");
    service.importBackup(backup, authentication);
    return ResponseEntity.ok().build();
  }
}
