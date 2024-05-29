package com.victorprado.financeappbackendjava.service.schedule;

import com.victorprado.financeappbackendjava.domain.entity.*;
import com.victorprado.financeappbackendjava.domain.repository.InvoiceRepository;
import com.victorprado.financeappbackendjava.domain.repository.MonthClosureRepository;
import com.victorprado.financeappbackendjava.domain.repository.TransactionRepository;
import com.victorprado.financeappbackendjava.domain.repository.UserRepository;
import com.victorprado.financeappbackendjava.service.UserService;
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

import static com.victorprado.financeappbackendjava.domain.enums.UserProperty.DOLLAR_COTATION;
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
        userRepository.findAllUsersWithMonthClosureTodayAndWithoutMonthClosuresOnMonthAndYear(String.valueOf(today.getDayOfMonth()), today.getMonth().name().substring(0, 3), today.getYear())
                .parallelStream()
                .forEach(this::calculateFinances);
    }

    protected void calculateFinances(User user) {
        var today = LocalDate.now();
        var monthName = today.getMonth().name().substring(0, 3);
        var invoices = invoiceRepository.findByMonthAndYearAndCreditCardIn(monthName, today.getYear(), user.getCreditCards());

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

        userService.executeCurrencyExchange(user, Optional.empty());

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

        repository.save(monthClosure);
    }
}
