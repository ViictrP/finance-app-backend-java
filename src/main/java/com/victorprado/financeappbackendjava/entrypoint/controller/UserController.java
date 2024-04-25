package com.victorprado.financeappbackendjava.entrypoint.controller;

import com.victorprado.financeappbackendjava.service.UserService;
import com.victorprado.financeappbackendjava.service.dto.ProfileCriteria;
import com.victorprado.financeappbackendjava.service.dto.UserBalanceDTO;
import com.victorprado.financeappbackendjava.service.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

  @GetMapping("/balance")
  public ResponseEntity<UserBalanceDTO> getBalance(ProfileCriteria criteria) {
    log.info("Balance request received");
    var balance = service.getUserBalance(criteria);
    return ResponseEntity.ok().body(balance);
  }
}
