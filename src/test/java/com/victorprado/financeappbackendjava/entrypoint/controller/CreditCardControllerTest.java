package com.victorprado.financeappbackendjava.entrypoint.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.victorprado.financeappbackendjava.Application;
import com.victorprado.financeappbackendjava.service.dto.CreditCardDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.victorprado.financeappbackendjava.domain.roles.Roles.ROLE_ADMIN;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {Application.class}
)
@ActiveProfiles(profiles = "test")
@AutoConfigureMockMvc
@WithMockUser(authorities = ROLE_ADMIN)
class CreditCardControllerTest {

    private static final String ENDPOINT = "/v1/credit-cards";

    @Autowired
    MockMvc mockMvc;

    final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("Should create a new credit card")
    void test1() throws Exception {
        mockMvc.perform(post(ENDPOINT)
                        .with(jwt().authorities(() -> ROLE_ADMIN))
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(getBody())))
                .andDo(print()).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should not create a new credit card with wrong field")
    void test2() throws Exception {
        var dto = getBody();
        dto.setNumber(null);
        mockMvc.perform(post(ENDPOINT)
                        .with(jwt().authorities(() -> ROLE_ADMIN))
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.number").value("The number is required"));
    }

    private CreditCardDTO getBody() {
        var dto = new CreditCardDTO();
        dto.setTitle("Title");
        dto.setNumber("1234");
        dto.setDescription("Description");
        dto.setInvoiceClosingDay(10);
        return dto;
    }
}
