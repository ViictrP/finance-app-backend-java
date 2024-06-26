package com.victorprado.financeappbackendjava.service;

import com.victorprado.financeappbackendjava.domain.repository.CreditCardRepository;
import com.victorprado.financeappbackendjava.domain.repository.RecurringExpenseRepository;
import com.victorprado.financeappbackendjava.domain.repository.TransactionRepository;
import com.victorprado.financeappbackendjava.service.dto.ProfileCriteria;
import com.victorprado.financeappbackendjava.service.mapper.CreditCardMapperImpl;
import com.victorprado.financeappbackendjava.service.mapper.InvoiceMapperImpl;
import com.victorprado.financeappbackendjava.service.mapper.RecurringExpenseMapperImpl;
import com.victorprado.financeappbackendjava.service.mapper.TransactionMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

//TODO: fix unit tests
@SpringJUnitConfig(classes = {
  UserService.class,
  CreditCardMapperImpl.class,
  InvoiceMapperImpl.class,
  TransactionMapperImpl.class,
  RecurringExpenseMapperImpl.class
})
class UserServiceTest {
  @MockBean
  Authentication authentication;
  @MockBean
  CreditCardRepository creditCardRepository;
  @MockBean
  TransactionRepository transactionRepository;
  @MockBean
  RecurringExpenseRepository recurringExpenseRepository;
  @Autowired
  UserService service;
  Map<String, Object> claims;

  @BeforeEach
  public void beforeEach() {
    var headers = new HashMap<String, Object>();
    headers.put("header", "header");
    claims = new HashMap<>();
    claims.put("sub", "ID");
    claims.put("given_name", "Name");
    claims.put("family_name", "Last name");
    claims.put("email", "email@email.com");
    when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
      .thenReturn(new Jwt("123", null, null, headers, claims));
  }

  @Test
  @DisplayName("Should return user by the given authentication")
  void test1() {
    var user = service.getUser(new ProfileCriteria());
    assertThat(user.getId()).isEqualTo(claims.get("sub"));
    assertThat(user.getName()).isEqualTo(claims.get("given_name"));
    assertThat(user.getLastname()).isEqualTo(claims.get("family_name"));
    assertThat(user.getEmail()).isEqualTo(claims.get("email"));
  }
}
