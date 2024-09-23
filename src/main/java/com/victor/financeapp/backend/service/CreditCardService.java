package com.victor.financeapp.backend.service;

import com.victor.financeapp.backend.domain.entity.Invoice;
import com.victor.financeapp.backend.domain.exception.CreditCardAlreadyExistsException;
import com.victor.financeapp.backend.domain.exception.UserNotFoundException;
import com.victor.financeapp.backend.domain.repository.CreditCardRepository;
import com.victor.financeapp.backend.domain.repository.UserRepository;
import com.victor.financeapp.backend.service.context.SecurityContext;
import com.victor.financeapp.backend.service.dto.CreditCardDTO;
import com.victor.financeapp.backend.service.dto.UpdateCreditCardDTO;
import com.victor.financeapp.backend.service.mapper.CreditCardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreditCardService {

    private final CreditCardRepository repository;
    private final CreditCardMapper mapper;
    private final UserRepository userRepository;

    @Transactional
    public CreditCardDTO create(CreditCardDTO dto) {
        validateCreditCardNumber(dto.getNumber());

        log.info("Converting the credit card {}", dto.getTitle());
        var entity = mapper.toEntity(dto);

        log.info("Getting user {} information", SecurityContext.getUserEmail());
        var user = userRepository.findByEmail(SecurityContext.getUserEmail())
                .orElseThrow(UserNotFoundException::new);

        user.addCreditCard(entity);

        var today = LocalDate.now();
        var invoice = Invoice.builder()
                .creditCard(entity)
                .month(today.getMonth().name().substring(0, 3))
                .year(today.getYear())
                .isClosed(false)
                .transactions(Collections.emptyList())
                .build();

        entity.setInvoices(List.of(invoice));

        log.info("Creating the credit card {}", dto.getTitle());
        repository.save(entity);
        return mapper.toDTO(entity);
    }
    
    @Transactional
    public CreditCardDTO update(Long creditCardId, UpdateCreditCardDTO dto) {
        log.info("Loading the credit card {} information", dto.getTitle());
        var creditCard = repository.findById(creditCardId)
                .orElseThrow(UserNotFoundException::new);

        if (!creditCard.getNumber().equals(dto.getNumber())) {
            validateCreditCardNumber(dto.getNumber());
        }

        creditCard.setDescription(dto.getDescription());
        creditCard.setBackgroundColor(dto.getBackgroundColor());
        creditCard.setInvoiceClosingDay(dto.getInvoiceClosingDay() != null ? dto.getInvoiceClosingDay() : creditCard.getInvoiceClosingDay());
        creditCard.setTitle(dto.getTitle());
        creditCard.setNumber(dto.getNumber() != null ? dto.getNumber() : creditCard.getNumber());

        log.info("Saving the credit card {} information", dto.getTitle());
        var savedCreditCard = repository.save(creditCard);
        return mapper.toDTOWithoutInvoices(savedCreditCard);
    }

    @Transactional
    public void delete(Long creditCardId) {
        log.info("Loading the credit card information");
        var creditCard = repository.findById(creditCardId)
                .orElseThrow(UserNotFoundException::new);

        creditCard.setDeleted(true);
        log.info("Deleting the credit card {}", creditCard.getTitle());
        repository.save(creditCard);
    }

    private void validateCreditCardNumber(String number) {
        log.info("Validating the credit card {} number", number);
        var hasCreditCard = repository.existsByNumber(number);

        if (hasCreditCard) {
            log.info("The credit card {} already exists", number);
            throw new CreditCardAlreadyExistsException();
        }
    }
}
