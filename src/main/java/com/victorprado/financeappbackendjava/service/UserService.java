package com.victorprado.financeappbackendjava.service;

import com.victorprado.financeappbackendjava.domain.repository.CreditCardRepository;
import com.victorprado.financeappbackendjava.domain.repository.RecurringExpenseRepository;
import com.victorprado.financeappbackendjava.domain.repository.TransactionRepository;
import com.victorprado.financeappbackendjava.service.dto.UserDTO;
import com.victorprado.financeappbackendjava.service.mapper.CreditCardMapper;
import com.victorprado.financeappbackendjava.service.mapper.RecurringExpenseMapper;
import com.victorprado.financeappbackendjava.service.mapper.TransactionMapper;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

  private final CreditCardRepository creditCardRepository;
  private final TransactionRepository transactionRepository;
  private final RecurringExpenseRepository recurringExpenseRepository;
  private final CreditCardMapper creditCardMapper;
  private final TransactionMapper transactionMapper;
  private final RecurringExpenseMapper recurringExpenseMapper;

  @Cacheable(cacheNames = "get_user_profile_cache")
  public UserDTO getUser(Authentication authentication) {
    var jwt = (Jwt) authentication.getPrincipal();
    var userId = (String) jwt.getClaim("sub");
    var email = jwt.getClaim("email");
    log.info("Getting credit cards and transactions of the user [user: {}]", email);
    var today = LocalDate.now();
    var creditCards = creditCardRepository.findByUserAndInvoicesByMonthAndYear(userId, today.getMonth().name(), today.getYear());
    var transactions = transactionRepository.findByUserIdAndInvoiceIsNull(userId);
    var recurringExpenses = recurringExpenseRepository.findByUserId(userId);
    log.info("Building user object with all fetched data [user: {}]", email);
    return UserDTO.builder()
      .id(userId)
      .name(jwt.getClaim("given_name"))
      .lastname(jwt.getClaim("family_name"))
      .email((String) email)
      .creditCards(creditCardMapper.toDTO(creditCards))
      .transactions(transactionMapper.toDTO(transactions))
      .recurringExpenses(recurringExpenseMapper.toDTO(recurringExpenses))
      .build();
  }
}
