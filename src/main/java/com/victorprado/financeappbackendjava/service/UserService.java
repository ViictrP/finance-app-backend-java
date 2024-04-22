package com.victorprado.financeappbackendjava.service;

import com.victorprado.financeappbackendjava.domain.entity.RecurringExpense;
import com.victorprado.financeappbackendjava.domain.entity.Transaction;
import com.victorprado.financeappbackendjava.domain.enums.MonthEnum;
import com.victorprado.financeappbackendjava.domain.exception.CoreException;
import com.victorprado.financeappbackendjava.domain.exception.LoggedUserNotFoundInBackup;
import com.victorprado.financeappbackendjava.domain.repository.CreditCardRepository;
import com.victorprado.financeappbackendjava.domain.repository.RecurringExpenseRepository;
import com.victorprado.financeappbackendjava.domain.repository.TransactionRepository;
import com.victorprado.financeappbackendjava.domain.repository.UserRepository;
import com.victorprado.financeappbackendjava.service.context.SecurityContext;
import com.victorprado.financeappbackendjava.service.dto.BackupDTO;
import com.victorprado.financeappbackendjava.service.dto.ProfileCriteria;
import com.victorprado.financeappbackendjava.service.dto.UserBalanceDTO;
import com.victorprado.financeappbackendjava.service.dto.UserDTO;
import com.victorprado.financeappbackendjava.service.mapper.CreditCardMapper;
import com.victorprado.financeappbackendjava.service.mapper.RecurringExpenseMapper;
import com.victorprado.financeappbackendjava.service.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final CreditCardRepository creditCardRepository;
    private final TransactionRepository transactionRepository;
    private final RecurringExpenseRepository recurringExpenseRepository;
    private final UserRepository repository;

    private final CreditCardMapper creditCardMapper;
    private final TransactionMapper transactionMapper;
    private final RecurringExpenseMapper recurringExpenseMapper;

    @Cacheable(cacheNames = "get_user_profile_cache")
    public UserDTO getUser(ProfileCriteria criteria) {
        var userDTO = SecurityContext.getUser();
        log.info("Loading user data [user: {}]", userDTO.getEmail());

        var user = repository.findByEmail(userDTO.getEmail())
                .orElseThrow(() -> new CoreException(HttpStatus.NOT_FOUND, "User not found"));

        log.info("Getting credit cards and transactions of the user [user: {}]", user.getEmail());
        var today = LocalDate.now();
        var month = criteria.getMonth() != null ? MonthEnum.getMonth(criteria.getMonth()) : MonthEnum.getMonth(today.getMonthValue());
        var year = criteria.getYear() != null ? criteria.getYear() : today.getYear();
        var creditCards = creditCardRepository.findByUserAndInvoicesByMonthAndYear(user.getId(), month.name(), year);

        var from = today
                .withYear(year)
                .withMonth(month.getIndex())
                .with(firstDayOfMonth()).atStartOfDay();

        var to = today
                .withMonth(month.getIndex())
                .withYear(year)
                .with(lastDayOfMonth())
                .atTime(23, 59, 59);

        var transactions = transactionRepository.findByUserIdAndInvoiceIsNullAndDateIsBetween(user.getId(),
                from, to);
        var recurringExpenses = recurringExpenseRepository.findByUserId(user.getId());
        log.info("Building user object with all fetched data [user: {}]", user.getEmail());
        userDTO.setCreditCards(creditCardMapper.toDTO(creditCards));
        userDTO.setTransactions(transactionMapper.toDTO(transactions));
        userDTO.setRecurringExpenses(recurringExpenseMapper.toDTO(recurringExpenses));
        return userDTO;
    }

    @Transactional
    public void importBackup(BackupDTO backup) {
        var userDto = SecurityContext.getUser();
        log.info("Verifying if logged user is present in the backup. User ID: {}", userDto.getName());
        var user = backup.getUsers().stream().filter(u -> userDto.getEmail().equals(u.getEmail()))
                .findFirst().orElseThrow(LoggedUserNotFoundInBackup::new);

        if (user.getCreditCards() != null) {
            log.info("Converting the credit cards. User ID: {}", userDto.getName());
            var creditCards = creditCardMapper.toEntity(user.getCreditCards());
            creditCards.forEach(creditCard -> {
                creditCard.getInvoices().forEach(invoice -> {
                    invoice.setCreditCard(creditCard);
                    invoice.getTransactions().forEach(transaction -> transaction.setInvoice(invoice));
                });
            });

            creditCardRepository.saveAll(creditCards);
        }

        if (user.getRecurringExpenses() != null) {
            log.info("Converting the recurring expenses. User ID: {}", userDto.getName());
            var recurringExpenses = recurringExpenseMapper.toEntity(user.getRecurringExpenses());

            recurringExpenseRepository.saveAll(recurringExpenses);
        }

        if (user.getTransactions() != null) {
            log.info("Converting the transactions. User ID: {}", userDto.getName());
            var transactions = transactionMapper.toEntity(user.getTransactions());

            transactionRepository.saveAll(transactions);
        }


    }

    @Cacheable(cacheNames = "get_user_balance_cache")
    public UserBalanceDTO getUserBalance(ProfileCriteria criteria) {
        var userDTO = SecurityContext.getUser();
        log.info("Loading user data [user: {}]", userDTO.getEmail());
        var user = repository.findByEmail(userDTO.getEmail())
                .orElseThrow(() -> new CoreException(HttpStatus.NOT_FOUND, "User not found"));

        log.info("Getting credit cards and transactions of the user [user: {}]", user.getEmail());
        var today = LocalDate.now();
        var month = criteria.getMonth() != null ? MonthEnum.getMonth(criteria.getMonth()) : MonthEnum.getMonth(today.getMonthValue());
        var year = criteria.getYear();
        var creditCards = creditCardRepository.findByUserAndInvoicesByMonthAndYear(user.getId(), month.name(), year);

        final var creditCardsTotal = new BigDecimal[]{BigDecimal.ZERO};
        var creditCardsAmounts = new HashMap<Long, BigDecimal>();
        creditCards.forEach(creditCard -> {
            var amount = creditCard.getInvoices().stream()
                    .map(invoice -> invoice.getTransactions().stream()
                            .map(Transaction::getAmount)
                            .reduce(BigDecimal::add)
                            .orElse(BigDecimal.ZERO))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            creditCardsAmounts.put(creditCard.getId(), amount);
            creditCardsTotal[0] = creditCardsTotal[0].add(amount);
        });

        var from = today
                .withYear(year)
                .withMonth(month.getIndex())
                .with(firstDayOfMonth()).atStartOfDay();

        var to = today
                .withMonth(month.getIndex())
                .withYear(year)
                .with(lastDayOfMonth())
                .atTime(23, 59, 59);

        var transactions = transactionRepository.findByUserIdAndInvoiceIsNullAndDateIsBetween(user.getId(), from, to);
        var transactionsTotal = transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        var recurringExpenses = recurringExpenseRepository.findByUserId(user.getId());
        var recurringExpensesTotal = recurringExpenses.stream()
                .map(RecurringExpense::getAmount)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        var expenses = creditCardsTotal[0].add(transactionsTotal).add(recurringExpensesTotal);

        return UserBalanceDTO.builder()
                .expenses(expenses)
                .salary(user.getSalary())
                .available(user.getSalary().subtract(expenses))
                .creditCardExpenses(creditCardsAmounts)
                .creditCards(creditCardMapper.toDTO(creditCards))
                .transactions(transactionMapper.toDTO(transactions))
                .recurringExpenses(recurringExpenseMapper.toDTO(recurringExpenses))
                .build();
    }
}
