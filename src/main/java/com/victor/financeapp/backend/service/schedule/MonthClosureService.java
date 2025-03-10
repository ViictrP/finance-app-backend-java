package com.victor.financeapp.backend.service.schedule;

import com.victor.financeapp.backend.domain.entity.*;
import com.victor.financeapp.backend.domain.repository.InvoiceRepository;
import com.victor.financeapp.backend.domain.repository.MonthClosureRepository;
import com.victor.financeapp.backend.domain.repository.TransactionRepository;
import com.victor.financeapp.backend.domain.repository.UserRepository;
import com.victor.financeapp.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.victor.financeapp.backend.domain.enums.UserProperty.DOLLAR_COTATION;
import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MonthClosureService {

    private final UserRepository userRepository;
    private final InvoiceRepository invoiceRepository;
    private final TransactionRepository transactionRepository;
    private final MonthClosureRepository repository;

    private final UserService userService;

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.DAYS)
    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor=Exception.class)
    public void runMonthClosures() {
        var today = LocalDate.now();
        log.info("Running month closures for {}", today);
        userRepository.findAllUsersWithMonthClosureTodayAndWithoutMonthClosuresOnMonthAndYear(String.valueOf(today.getDayOfMonth()), today.getMonth().name().substring(0, 3), today.getYear())
                .parallelStream()
                .forEach(this::calculateFinances);
    }

    protected void calculateFinances(User user) {
        log.info("Calculating finances for user {}", user.getName());
        var today = LocalDate.now();
        var monthName = today.getMonth().name().substring(0, 3);

        log.info("Loading user {}'s invoices", user.getName());
        var invoices = invoiceRepository.findByMonthAndYearAndCreditCardIn(monthName, today.getYear(), user.getCreditCards());

        log.info("Processing user {}'s data", user.getName());
        var totalInvoiceTransactions = invoices.stream()
                .map(Invoice::getTransactions)
                .flatMap(Collection::stream)
                .map(Transaction::getAmount)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        var totalRecurringExpenses = user.getRecurringExpenses()
                .stream().map(RecurringExpense::getAmount)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);


        var from = today
                .withYear(today.getYear())
                .withMonth(today.getMonthValue())
                .with(firstDayOfMonth()).atStartOfDay();

        var to = today
                .withMonth(today.getMonthValue())
                .withYear(today.getYear())
                .with(lastDayOfMonth())
                .atTime(23, 59, 59);

        var transactions = transactionRepository.findByUserIdAndInvoiceIsNullAndDateIsBetween(user.getId(), from, to);
        var totalTransactions = transactions
                .stream().map(Transaction::getAmount)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        var total = totalInvoiceTransactions.add(totalRecurringExpenses).add(totalTransactions);

        log.info("Loading dollar cotation for {}", today);
        userService.executeCurrencyExchange(user, Optional.empty());

        log.info("Building the response for user {}'s month clojure", user.getName());
        var monthClosure = MonthClosure.builder()
                .month(today.getMonth().name().substring(0, 3))
                .year(today.getYear())
                .index(today.getMonthValue())
                .total(user.getSalary())
                .expenses(total)
                .available(user.getSalary().subtract(total))
                .finalUsdToBRL(new BigDecimal(user.getProperty(DOLLAR_COTATION)))
                .user(user)
                .build();

        log.info("Saving user {}'s data", user.getName());
        repository.save(monthClosure);
    }
}
