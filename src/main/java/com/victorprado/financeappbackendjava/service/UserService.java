package com.victorprado.financeappbackendjava.service;

import com.victorprado.financeappbackendjava.service.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

  public UserDTO getUser(Authentication authentication) {
    var jwt = (Jwt) authentication.getPrincipal();
    var email = jwt.getClaim("email");
    log.info("Getting authenticated user {}", email);
    return UserDTO.builder()
      .id(jwt.getClaim("sub"))
      .name(jwt.getClaim("given_name"))
      .lastname(jwt.getClaim("family_name"))
      .email((String) email)
      .build();
  }
}
