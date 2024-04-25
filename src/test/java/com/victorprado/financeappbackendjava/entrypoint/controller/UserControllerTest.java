package com.victorprado.financeappbackendjava.entrypoint.controller;

import com.victorprado.financeappbackendjava.Application;
import com.victorprado.financeappbackendjava.domain.roles.Roles;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {Application.class}
)
@ActiveProfiles(profiles = "test")
@AutoConfigureMockMvc
@WithMockUser(authorities = Roles.ROLE_ADMIN)
class UserControllerTest {
    private static final String ENDPOINT = "/v1/users";

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("Should return logged user data")
    void test1() throws Exception {
        mockMvc.perform(get(ENDPOINT + "/me")
                        .with(jwt().authorities(() -> Roles.ROLE_ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value("user"));
    }
}
