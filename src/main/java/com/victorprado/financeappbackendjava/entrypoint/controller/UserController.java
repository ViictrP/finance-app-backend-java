package com.victorprado.financeappbackendjava.entrypoint.controller;

import com.victorprado.financeappbackendjava.service.UserService;
import com.victorprado.financeappbackendjava.service.dto.BackupDTO;
import com.victorprado.financeappbackendjava.service.dto.ProfileCriteria;
import com.victorprado.financeappbackendjava.service.dto.UserBalanceDTO;
import com.victorprado.financeappbackendjava.service.dto.UserDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import static com.victorprado.financeappbackendjava.domain.roles.Roles.ROLE_ADMIN;
import static com.victorprado.financeappbackendjava.domain.roles.Roles.ROLE_USER;

@Slf4j
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@CrossOrigin("*")
//@Secured({ROLE_USER, ROLE_ADMIN})
public class UserController {

  private final UserService service;

  @GetMapping("/me")
  public ResponseEntity<UserDTO> getProfile(ProfileCriteria criteria) {
    log.info("Get profile request received");
    return ResponseEntity.ok(service.getUser(criteria));
  }

  @PostMapping("/backups")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<Void> importBackup(@Valid @RequestBody BackupDTO backup) {
    log.info("Backup request received");
    service.importBackup(backup);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/balance")
  public ResponseEntity<UserBalanceDTO> getBalance(ProfileCriteria criteria) {
    log.info("Balance request received");
    var balance = service.getUserBalance(criteria);
    return ResponseEntity.ok().body(balance);
  }
}
