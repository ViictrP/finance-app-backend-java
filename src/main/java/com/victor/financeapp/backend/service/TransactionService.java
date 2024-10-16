package com.victor.financeapp.backend.service;

import com.victor.financeapp.backend.domain.entity.CreditCard;
import com.victor.financeapp.backend.domain.entity.Invoice;
import com.victor.financeapp.backend.domain.entity.Transaction;
import com.victor.financeapp.backend.domain.entity.User;
import com.victor.financeapp.backend.domain.exception.CreditCardNotFoundException;
import com.victor.financeapp.backend.domain.exception.TransactionNotFoundException;
import com.victor.financeapp.backend.domain.exception.UserNotFoundException;
import com.victor.financeapp.backend.domain.repository.CreditCardRepository;
import com.victor.financeapp.backend.domain.repository.InvoiceRepository;
import com.victor.financeapp.backend.domain.repository.TransactionRepository;
import com.victor.financeapp.backend.domain.repository.UserRepository;
import com.victor.financeapp.backend.service.context.SecurityContext;
import com.victor.financeapp.backend.service.dto.TransactionDTO;
import com.victor.financeapp.backend.service.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {

    private static final Integer NUMBER_OF_MONTHS_IN_YEAR = 12;

    private final TransactionRepository repository;
    private final InvoiceRepository invoiceRepository;
    private final CreditCardRepository creditCardRepository;
    private final UserRepository userRepository;
    private final TransactionMapper mapper;

    @Transactional
    public TransactionDTO create(TransactionDTO dto) {
        log.info("Creating transaction {}", dto.getDescription());
        var user = userRepository.findByEmail(SecurityContext.getUserEmail())
                .orElseThrow(UserNotFoundException::new);

        if (dto.getCreditCardId() != null) {
            log.info("Loading credit cards's {} data", dto.getCreditCardId());
            var creditCard = creditCardRepository.findById(dto.getCreditCardId())
                    .orElseThrow(CreditCardNotFoundException::new);

            log.info("Creating invoice's transaction {}", dto.getDescription());
            return createInvoiceTransaction(dto, creditCard, user);
        }

        var now = LocalDateTime.now();
        var entity = mapper.toEntity(dto);
        entity.setDate(entity.getDate().withHour(now.getHour()).withMinute(now.getMinute()).withSecond(now.getSecond()));
        entity.setUser(user);
        var saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    @Transactional
    public void delete(Long transactionId, Boolean deleteAll) {
        log.info("Loading the transaction {} data", transactionId);
        var transaction = repository.findById(transactionId)
                .orElseThrow(TransactionNotFoundException::new);

        if (deleteAll) {
            repository.findAllByInstallmentId(transaction.getInstallmentId())
                    .stream().peek(Transaction::delete)
                    .parallel().forEach(repository::save);
            return;
        }

        transaction.delete();
        repository.save(transaction);
    }

    private TransactionDTO createInvoiceTransaction(TransactionDTO dto, CreditCard creditCard, User user) {
        log.info("Creating installment transactions {}", dto.getDescription());
        var installmentId = UUID.randomUUID().toString();
        var installments = new ArrayList<Transaction>();

        log.info("Calculating transaction {}'s amount for the installment number", dto.getDescription());
        var transactionAmount = dto.getAmount().divide(BigDecimal.valueOf(dto.getInstallmentAmount()), RoundingMode.FLOOR);
        dto.setAmount(transactionAmount);

        var now = LocalDateTime.now();
        var monthIndex = dto.getDate().getMonthValue();

        for (var i = 0; i < dto.getInstallmentAmount(); i++) {
            var newTransaction = new Transaction();
            newTransaction.setDate(dto.getDate().withHour(now.getHour()).withMinute(now.getMinute()).withSecond(now.getSecond()));
            newTransaction.setAmount(dto.getAmount());
            newTransaction.setCategory(dto.getCategory());
            newTransaction.setUser(user);

            if (dto.getInstallmentAmount() > 1) {
                newTransaction.setIsInstallment(true);
                newTransaction.setDescription(dto.getDescription());
                newTransaction.setInstallmentNumber(i + 1);
                newTransaction.setInstallmentAmount(dto.getInstallmentAmount());
                newTransaction.setInstallmentId(installmentId);
            } else {
                newTransaction.setDescription(dto.getDescription());
            }

            populateWithInvoice(newTransaction, creditCard, monthIndex);
            installments.add(newTransaction);
            monthIndex++;
        }
        repository.saveAll(installments);
        return mapper.toDTO(installments.get(0));
    }

    private void populateWithInvoice(Transaction newTransaction, CreditCard creditCard, Integer monthIndex) {
        var year = newTransaction.getDate().getYear();
        var day = newTransaction.getDate().getDayOfMonth();


        if (day > creditCard.getInvoiceClosingDay()) {
            monthIndex++;
        }

        if (monthIndex > NUMBER_OF_MONTHS_IN_YEAR) {
            year++;
            monthIndex = monthIndex - NUMBER_OF_MONTHS_IN_YEAR;
        }

        var yearMonth = YearMonth.of(year, monthIndex);
        var newDate = LocalDate.of(year, monthIndex, Math.min(day, yearMonth.lengthOfMonth()));
        var nextMonth = newDate.getMonth().name().substring(0, 3);

        var invoice = invoiceRepository
                .findByMonthAndYearAndCreditCardId(nextMonth, year, creditCard.getId())
                .orElse(Invoice
                        .builder()
                        .month(nextMonth)
                        .creditCard(creditCard)
                        .year(year)
                        .isClosed(false)
                        .build());

        newTransaction.setInvoice(invoice);
    }
}
