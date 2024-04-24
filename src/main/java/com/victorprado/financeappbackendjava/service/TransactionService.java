package com.victorprado.financeappbackendjava.service;

import com.victorprado.financeappbackendjava.domain.entity.CreditCard;
import com.victorprado.financeappbackendjava.domain.entity.Invoice;
import com.victorprado.financeappbackendjava.domain.entity.Transaction;
import com.victorprado.financeappbackendjava.domain.entity.User;
import com.victorprado.financeappbackendjava.domain.exception.CreditCardNotFoundException;
import com.victorprado.financeappbackendjava.domain.exception.UserNotFoundException;
import com.victorprado.financeappbackendjava.domain.exception.TransactionNotFoundException;
import com.victorprado.financeappbackendjava.domain.repository.CreditCardRepository;
import com.victorprado.financeappbackendjava.domain.repository.InvoiceRepository;
import com.victorprado.financeappbackendjava.domain.repository.TransactionRepository;
import com.victorprado.financeappbackendjava.domain.repository.UserRepository;
import com.victorprado.financeappbackendjava.service.context.SecurityContext;
import com.victorprado.financeappbackendjava.service.dto.TransactionDTO;
import com.victorprado.financeappbackendjava.service.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {

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

        var entity = mapper.toEntity(dto);
        entity.setUser(user);
        var saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    @Transactional
    public void delete(Long transactionId) {
        log.info("Loading the transaction {} data", transactionId);
        var transaction = repository.findById(transactionId)
                .orElseThrow(TransactionNotFoundException::new);

        transaction.delete();
        repository.save(transaction);
    }

    private TransactionDTO createInvoiceTransaction(TransactionDTO dto, CreditCard creditCard, User user) {
        log.info("Creating installment transactions {}", dto.getDescription());
        var yearsIncrement = 0;
        var installmentId = UUID.randomUUID().toString();
        var installments = new ArrayList<Transaction>();

        log.info("Calculating transaction {}'s amount for the installment number", dto.getDescription());
        var transactionAmount = dto.getAmount().divide(BigDecimal.valueOf(dto.getInstallmentAmount()), RoundingMode.FLOOR);
        dto.setAmount(transactionAmount);
        var monthIndex = dto.getDate().getMonthValue();

        for (var i = 0; i < dto.getInstallmentAmount(); i++) {
            var newTransaction = new Transaction();

            if (monthIndex > 12) {
                monthIndex = 1;
                yearsIncrement++;
            }

            newTransaction.setDate(LocalDateTime.of(dto.getDate().getYear() + yearsIncrement, monthIndex, dto.getDate().getDayOfMonth(), dto.getDate().getHour(), dto.getDate().getMinute(), dto.getDate().getSecond()));

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

            populateWithInvoice(newTransaction, creditCard);
            installments.add(newTransaction);
            monthIndex++;
        }
        repository.saveAll(installments);
        return mapper.toDTO(installments.get(0));
    }

    private void populateWithInvoice(Transaction newTransaction, CreditCard creditCard) {
        var year = newTransaction.getDate().getYear();
        var month = newTransaction.getDate().getMonthValue();
        var day = newTransaction.getDate().getDayOfMonth();


        if (day > creditCard.getInvoiceClosingDay()) {
            month++;
        }

        if (month > 12) {
            year++;
            month = 0;
        }

        var date = LocalDate.of(year, month, day);
        var nextMonth = date.getMonth().name().substring(0, 3);

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
