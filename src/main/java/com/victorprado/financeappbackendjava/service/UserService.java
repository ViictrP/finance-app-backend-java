package com.victorprado.financeappbackendjava.service;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

import com.victorprado.financeappbackendjava.domain.enums.MonthEnum;
import com.victorprado.financeappbackendjava.domain.exception.InvalidSalaryException;
import com.victorprado.financeappbackendjava.domain.exception.LoggedUserNotFoundInBackup;
import com.victorprado.financeappbackendjava.domain.repository.CreditCardRepository;
import com.victorprado.financeappbackendjava.domain.repository.RecurringExpenseRepository;
import com.victorprado.financeappbackendjava.domain.repository.SalaryRepository;
import com.victorprado.financeappbackendjava.domain.repository.TransactionRepository;
import com.victorprado.financeappbackendjava.service.dto.BackupDTO;
import com.victorprado.financeappbackendjava.service.dto.ProfileCriteria;
import com.victorprado.financeappbackendjava.service.dto.SalaryDTO;
import com.victorprado.financeappbackendjava.service.dto.UserDTO;
import com.victorprado.financeappbackendjava.service.mapper.CreditCardMapper;
import com.victorprado.financeappbackendjava.service.mapper.RecurringExpenseMapper;
import com.victorprado.financeappbackendjava.service.mapper.SalaryMapper;
import com.victorprado.financeappbackendjava.service.mapper.TransactionMapper;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

  private final CreditCardRepository creditCardRepository;
  private final TransactionRepository transactionRepository;
  private final RecurringExpenseRepository recurringExpenseRepository;
  private final CreditCardMapper creditCardMapper;
  private final TransactionMapper transactionMapper;
  private final RecurringExpenseMapper recurringExpenseMapper;
  private final SalaryRepository salaryRepository;
  private final SalaryMapper salaryMapper;

  @Cacheable(cacheNames = "get_user_profile_cache")
  public UserDTO getUser(ProfileCriteria criteria, Authentication authentication) {
    var jwt = (Jwt) authentication.getPrincipal();
    var userId = (String) jwt.getClaim("sub");
    var email = jwt.getClaim("email");
    log.info("Getting credit cards and transactions of the user [user: {}]", email);
    var today = LocalDate.now();
    var month = criteria.getMonth() != null ? MonthEnum.getMonth(criteria.getMonth()) : MonthEnum.getMonth(today.getMonthValue());
    var year = criteria.getYear() != null ? criteria.getYear() : today.getYear();
    var creditCards = creditCardRepository.findByUserAndInvoicesByMonthAndYear(userId, month.name(), year);

    var from = today
      .withYear(year)
      .withMonth(month.getIndex())
      .with(firstDayOfMonth()).atStartOfDay();

    var to = today
      .withMonth(month.getIndex())
      .withYear(year)
      .with(lastDayOfMonth())
      .atTime(23, 59, 59);

    var transactions = transactionRepository.findByUserIdAndInvoiceIsNullAndDateIsBetween(userId,
      from, to);
    var recurringExpenses = recurringExpenseRepository.findByUserId(userId);
    var salary = salaryRepository.findByUserId(userId).orElse(null);
    log.info("Building user object with all fetched data [user: {}]", email);
    return UserDTO.builder()
      .id(userId)
      .name(jwt.getClaim("given_name"))
      .lastname(jwt.getClaim("family_name"))
      .email((String) email)
      .salary(salaryMapper.toDTO(salary))
      .creditCards(creditCardMapper.toDTO(creditCards))
      .transactions(transactionMapper.toDTO(transactions))
      .recurringExpenses(recurringExpenseMapper.toDTO(recurringExpenses))
      .build();
  }

  public SalaryDTO createSalary(SalaryDTO salary) {
    var entity = salaryMapper.toEntity(salary);
    if (!entity.validate()) {
      throw new InvalidSalaryException();
    }
    var result = salaryRepository.save(entity);
    return salaryMapper.toDTO(result);
  }

  @Transactional
  public void importBackup(BackupDTO backup, Authentication authentication) {
    log.info("Verifying if logged user is present in the backup. User ID: {}",
      authentication.getName());
    var userId = authentication.getName();
    var email = ((Jwt) authentication.getPrincipal()).getClaim("email");
    var user = backup.getUsers().stream().filter(u -> email.equals(u.getEmail()))
      .findFirst().orElseThrow(LoggedUserNotFoundInBackup::new);

    if (user.getCreditCards() != null) {
      log.info("Converting the credit cards. User ID: {}", authentication.getName());
      var creditCards = creditCardMapper.toEntity(user.getCreditCards());
      creditCards.forEach(creditCard -> {
        creditCard.setUserId(userId);
        creditCard.getInvoices().forEach(invoice -> {
          invoice.setCreditCard(creditCard);
          invoice.getTransactions().forEach(transaction -> transaction.setInvoice(invoice));
        });
      });

      creditCardRepository.saveAll(creditCards);
    }

    if (user.getRecurringExpenses() != null) {
      log.info("Converting the recurring expenses. User ID: {}", authentication.getName());
      var recurringExpenses = recurringExpenseMapper.toEntity(user.getRecurringExpenses());
      recurringExpenses.forEach(recurringExpense -> recurringExpense.setUserId(userId));

      recurringExpenseRepository.saveAll(recurringExpenses);
    }

    if (user.getTransactions() != null) {
      log.info("Converting the transactions. User ID: {}", authentication.getName());
      var transactions = transactionMapper.toEntity(user.getTransactions());
      transactions.forEach(transaction -> transaction.setUserId(userId));

      transactionRepository.saveAll(transactions);
    }


  }
}
