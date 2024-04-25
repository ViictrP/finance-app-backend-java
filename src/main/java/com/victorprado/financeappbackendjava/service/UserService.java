package com.victorprado.financeappbackendjava.service;

import com.victorprado.financeappbackendjava.client.CurrencyAPI;
import com.victorprado.financeappbackendjava.domain.entity.RecurringExpense;
import com.victorprado.financeappbackendjava.domain.entity.Transaction;
import com.victorprado.financeappbackendjava.domain.entity.User;
import com.victorprado.financeappbackendjava.domain.enums.MonthEnum;
import com.victorprado.financeappbackendjava.domain.exception.CoreException;
import com.victorprado.financeappbackendjava.domain.repository.*;
import com.victorprado.financeappbackendjava.service.context.SecurityContext;
import com.victorprado.financeappbackendjava.service.dto.ProfileCriteria;
import com.victorprado.financeappbackendjava.service.dto.UserBalanceDTO;
import com.victorprado.financeappbackendjava.service.dto.UserDTO;
import com.victorprado.financeappbackendjava.service.mapper.CreditCardMapper;
import com.victorprado.financeappbackendjava.service.mapper.MonthClosureMapper;
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
import java.util.Map;

import static com.victorprado.financeappbackendjava.client.enums.Context.USDBRL;
import static com.victorprado.financeappbackendjava.domain.enums.UserProperty.*;
import static java.lang.Boolean.parseBoolean;
import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final CurrencyAPI currencyAPI;

    private final CreditCardRepository creditCardRepository;
    private final TransactionRepository transactionRepository;
    private final RecurringExpenseRepository recurringExpenseRepository;
    private final MonthClosureRepository monthClosureRepository;
    private final UserRepository repository;

    private final CreditCardMapper creditCardMapper;
    private final TransactionMapper transactionMapper;
    private final RecurringExpenseMapper recurringExpenseMapper;
    private final MonthClosureMapper monthClosureMapper;

    @Cacheable(cacheNames = "get_user_profile_cache")
    public UserDTO getUser(ProfileCriteria criteria) {
        var user = loadUser();

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

        var monthClosures = monthClosureRepository.findLastFiveByYear(user, today.getYear());

        log.info("Building user object with all fetched data [user: {}]", user.getEmail());
        return UserDTO.builder()
                .id(SecurityContext.getUserId())
                .name(SecurityContext.getUserName())
                .lastname(SecurityContext.getUserLastName())
                .email(SecurityContext.getUserEmail())
                .salary(user.getSalary())
                .taxValue(user.getTaxValue())
                .exchangeTaxValue(user.getExchangeTaxValue())
                .nonConvertedSalary(user.getNonConvertedSalary())
                .creditCards(creditCardMapper.toDTO(creditCards))
                .transactions(transactionMapper.toDTO(transactions))
                .monthClosures(monthClosureMapper.toDTO(monthClosures))
                .recurringExpenses(recurringExpenseMapper.toDTO(recurringExpenses))
                .build();
    }

    @Cacheable(cacheNames = "get_user_balance_cache")
    public UserBalanceDTO getUserBalance(ProfileCriteria criteria) {
        var user = loadUser();

        log.info("Getting logged user's credit cards and transactions [user: {}]", user.getEmail());
        var today = LocalDate.now();
        var month = criteria.getMonth() != null ? MonthEnum.getMonth(criteria.getMonth()) : MonthEnum.getMonth(today.getMonthValue());
        var year = criteria.getYear() != null ? criteria.getYear() : today.getYear();
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
                .taxValue(user.getTaxValue())
                .exchangeTaxValue(user.getExchangeTaxValue())
                .nonConvertedSalary(user.getNonConvertedSalary())
                .creditCardExpenses(creditCardsAmounts)
                .creditCards(creditCardMapper.toDTO(creditCards))
                .transactions(transactionMapper.toDTO(transactions))
                .recurringExpenses(recurringExpenseMapper.toDTO(recurringExpenses))
                .build();
    }

    private User loadUser() {
        log.info("Loading user data [user: {}]", SecurityContext.getUserEmail());
        return repository.findByEmail(SecurityContext.getUserEmail())
                .map(u -> {
                    executeCurrencyExchange(u);
                    return u;
                })
                .orElseThrow(() -> new CoreException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private void executeCurrencyExchange(User user) {
        var mustDoConversion = user.getProperty(CURRENCY_CONVERSION);

        if (parseBoolean(mustDoConversion)) {
            var exchangeType = user.getProperty(CURRENCY_CONVERSION_TYPE);
            var exchangeRate = currencyAPI.getDollarExchangeRates(exchangeType);
            var usdToBrl = (Map<String, String>) exchangeRate.getBody().get(USDBRL.getType());

            if (usdToBrl != null) {
                var exchangeValue = new BigDecimal(usdToBrl.get("ask"));
                var grossSalary = exchangeValue.multiply(user.getSalary());
                var currencyConversionTax = new BigDecimal(user.getProperty(CURRENCY_CONVERSION_TAX));

                var salaryTax = new BigDecimal(user.getProperty(SALARY_TAX));
                var salaryMinusCurrencyTax = grossSalary.subtract(grossSalary.multiply(currencyConversionTax));

                user.setNonConvertedSalary(user.getSalary());
                user.setSalary(salaryMinusCurrencyTax.subtract(salaryMinusCurrencyTax.multiply(salaryTax)));
                user.setTaxValue(salaryMinusCurrencyTax.multiply(salaryTax));
                user.setExchangeTaxValue(grossSalary.multiply(currencyConversionTax));
            }
        }
    }
}
