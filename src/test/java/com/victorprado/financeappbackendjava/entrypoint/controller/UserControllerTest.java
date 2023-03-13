package com.victorprado.financeappbackendjava.entrypoint.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.victorprado.financeappbackendjava.service.UserService;
import com.victorprado.financeappbackendjava.service.dto.UserDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@ActiveProfiles(profiles = {"dev"})
class UserControllerTest {
  private static final String ENDPOINT = "/v1/users";

  @MockBean
  UserService service;

  @Autowired
  MockMvc mockMvc;

  @Test
  @DisplayName("Should return logged user data")
  void test1() throws Exception {
    when(service.getUser(any())).thenReturn(dto());

    mockMvc.perform(get(ENDPOINT + "/me")
      .with(jwt()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value("ID"))
      .andExpect(jsonPath("$.name").value("NAME"))
      .andExpect(jsonPath("$.lastname").value("LAST_NAME"))
      .andExpect(jsonPath("$.email").value("EMAIL"));
  }

  private static UserDTO dto() {
    return UserDTO.builder()
      .id("ID")
      .name("NAME")
      .lastname("LAST_NAME")
      .email("EMAIL")
      .build();
  }
}
