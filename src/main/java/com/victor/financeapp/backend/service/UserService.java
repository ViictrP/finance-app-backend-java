package com.victor.financeapp.backend.service;

import com.victor.financeapp.backend.client.CurrencyAPI;
import com.victor.financeapp.backend.domain.entity.MonthClosure;
import com.victor.financeapp.backend.domain.entity.RecurringExpense;
import com.victor.financeapp.backend.domain.entity.Transaction;
import com.victor.financeapp.backend.domain.entity.User;
import com.victor.financeapp.backend.domain.enums.MonthEnum;
import com.victor.financeapp.backend.domain.exception.UserNotFoundException;
import com.victor.financeapp.backend.domain.repository.CreditCardRepository;
import com.victor.financeapp.backend.domain.repository.MonthClosureRepository;
import com.victor.financeapp.backend.domain.repository.RecurringExpenseRepository;
import com.victor.financeapp.backend.domain.repository.TransactionRepository;
import com.victor.financeapp.backend.domain.repository.UserRepository;
import com.victor.financeapp.backend.service.context.SecurityContext;
import com.victor.financeapp.backend.service.dto.ProfileCriteria;
import com.victor.financeapp.backend.service.dto.ProfileDTO;
import com.victor.financeapp.backend.service.dto.UpdateProfileDTO;
import com.victor.financeapp.backend.service.dto.UserBalanceDTO;
import com.victor.financeapp.backend.service.dto.UserDTO;
import com.victor.financeapp.backend.service.mapper.CreditCardMapper;
import com.victor.financeapp.backend.service.mapper.MonthClosureMapper;
import com.victor.financeapp.backend.service.mapper.RecurringExpenseMapper;
import com.victor.financeapp.backend.service.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.victor.financeapp.backend.client.enums.Context.USDBRL;
import static com.victor.financeapp.backend.domain.enums.UserProperty.CURRENCY_CONVERSION;
import static com.victor.financeapp.backend.domain.enums.UserProperty.CURRENCY_CONVERSION_TAX;
import static com.victor.financeapp.backend.domain.enums.UserProperty.CURRENCY_CONVERSION_TYPE;
import static com.victor.financeapp.backend.domain.enums.UserProperty.DOLLAR_COTATION;
import static com.victor.financeapp.backend.domain.enums.UserProperty.SALARY_TAX;
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
        log.info("Getting credit cards and transactions of the user [user: {}]", SecurityContext.getUserEmail());
        var today = LocalDate.now();
        var month = criteria.getMonth() != null ? MonthEnum.getMonth(criteria.getMonth()) : MonthEnum.getMonth(today.getMonthValue());
        var year = criteria.getYear() != null ? criteria.getYear() : today.getYear();

        var from = today
                .withYear(year)
                .withMonth(month.getIndex())
                .with(firstDayOfMonth()).atStartOfDay();

        var to = today
                .withMonth(month.getIndex())
                .withYear(year)
                .with(lastDayOfMonth())
                .atTime(23, 59, 59);

        var currentMonthClosure = monthClosureRepository.findByMonthAndYearAndUser_Email(month.name(), today.getYear(), SecurityContext.getUserEmail());
        var user = loadUser(currentMonthClosure);
        var creditCards = creditCardRepository.findByUserAndInvoicesByMonthAndYear(user.getId(), month.name(), year);
        var transactions = transactionRepository.findByUserIdAndInvoiceIsNullAndDateIsBetween(user.getId(), from, to);
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
                .properties(user.getProperties())
                .build();
    }

    @Cacheable(cacheNames = "get_user_balance_cache")
    public UserBalanceDTO getUserBalance(ProfileCriteria criteria) {
        log.info("Getting logged user's credit cards and transactions [user: {}]", SecurityContext.getUserEmail());
        var today = LocalDate.now();
        var month = criteria.getMonth() != null ? MonthEnum.getMonth(criteria.getMonth()) : MonthEnum.getMonth(today.getMonthValue());
        var year = criteria.getYear() != null ? criteria.getYear() : today.getYear();

        var currentMonthClosure = monthClosureRepository.findByMonthAndYearAndUser_Email(month.name(), today.getYear(), SecurityContext.getUserEmail());
        var user = loadUser(currentMonthClosure);
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

        BigDecimal expenses;
        if (currentMonthClosure.isPresent()) {
            expenses = currentMonthClosure.get().getExpenses();
        } else {
            expenses = creditCardsTotal[0].add(transactionsTotal).add(recurringExpensesTotal);
        }

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

    private User loadUser(Optional<MonthClosure> withMonthClosure) {
        log.info("Loading user data [user: {}]", SecurityContext.getUserEmail());
        return repository.findByEmail(SecurityContext.getUserEmail())
                .map(u -> {
                    executeCurrencyExchange(u, withMonthClosure);
                    return u;
                })
                .orElseThrow(UserNotFoundException::new);
    }

    public void executeCurrencyExchange(User user, Optional<MonthClosure> withMonthClosure) {
        var mustDoConversion = user.getProperty(CURRENCY_CONVERSION);

        if (parseBoolean(mustDoConversion)) {
            var exchangeType = user.getProperty(CURRENCY_CONVERSION_TYPE);

            BigDecimal usdToBrl;

            if (withMonthClosure.isPresent()) {
                usdToBrl = withMonthClosure.get().getFinalUsdToBRL();
            } else {
                var exchangeRate = currencyAPI.getDollarExchangeRates(exchangeType);
                var  _usdToBrl = (Map<String, String>) exchangeRate.get(USDBRL.getType());
                usdToBrl = _usdToBrl != null ? new BigDecimal(_usdToBrl.get("ask")) : BigDecimal.ONE;
            }


            user.getProperties().put(DOLLAR_COTATION.name(), usdToBrl.toString());
            var grossSalary = usdToBrl.multiply(user.getSalary());
            user.setNonConvertedSalary(user.getSalary());

            if (user.getProperty(CURRENCY_CONVERSION_TAX) != null) {
                var currencyConversionTax = new BigDecimal(user.getProperty(CURRENCY_CONVERSION_TAX));
                var conversionValue = grossSalary.multiply(currencyConversionTax);
                grossSalary = grossSalary.subtract(conversionValue);
                user.setExchangeTaxValue(conversionValue);
            }

            if (user.getProperty(SALARY_TAX) != null) {
                var salaryTax = new BigDecimal(user.getProperty(SALARY_TAX));
                var taxValue = grossSalary.multiply(salaryTax);
                grossSalary = grossSalary.subtract(grossSalary.multiply(salaryTax));
                user.setTaxValue(taxValue);
            }

            user.setSalary(grossSalary);
        }
    }

    @Transactional
    public UserDTO create(ProfileDTO profile) {
        var user = User.builder()
                .name(profile.getName())
                .lastname(profile.getLastname())
                .email(profile.getEmail())
                .salary(profile.getSalary())
                .active(true)
                .properties(profile.getProperties())
                .build();

        log.info("Creating profile [user: {}]", user.getEmail());
        repository.save(user);
        return UserDTO.builder()
                .id(user.getId().toString())
                .name(user.getName())
                .lastname(user.getLastname())
                .email(SecurityContext.getUserEmail())
                .salary(user.getSalary())
                .taxValue(user.getTaxValue())
                .exchangeTaxValue(user.getExchangeTaxValue())
                .nonConvertedSalary(user.getNonConvertedSalary())
                .build();
    }

    @Transactional
    public UserDTO update(UpdateProfileDTO profile) {
        log.info("Loading user's data [user: {}]", SecurityContext.getUserEmail());
        var user = repository.findByEmail(SecurityContext.getUserEmail())
                .orElseThrow(UserNotFoundException::new);

        user.setProperties(profile.getProperties());

        log.info("Updating the profile [user: {}]", user.getEmail());
        repository.save(user);
        return UserDTO.builder()
                .id(user.getId().toString())
                .name(user.getName())
                .lastname(user.getLastname())
                .email(SecurityContext.getUserEmail())
                .salary(user.getSalary())
                .taxValue(user.getTaxValue())
                .exchangeTaxValue(user.getExchangeTaxValue())
                .nonConvertedSalary(user.getNonConvertedSalary())
                .properties(user.getProperties())
                .build();
    }
}
