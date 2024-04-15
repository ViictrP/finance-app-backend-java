package com.victorprado.financeappbackendjava.service;

import com.victorprado.financeappbackendjava.domain.entity.Invoice;
import com.victorprado.financeappbackendjava.domain.exception.CreditCardAlreadyExistsException;
import com.victorprado.financeappbackendjava.domain.exception.UserNotFoundException;
import com.victorprado.financeappbackendjava.domain.repository.CreditCardRepository;
import com.victorprado.financeappbackendjava.domain.repository.UserRepository;
import com.victorprado.financeappbackendjava.entrypoint.controller.context.SecurityContext;
import com.victorprado.financeappbackendjava.service.dto.CreditCardDTO;
import com.victorprado.financeappbackendjava.service.mapper.CreditCardMapper;
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
        log.info("Validating the credit card {} information", dto.getTitle());
        var hasCreditCard = repository.existsByNumber(dto.getNumber());

        if (hasCreditCard) {
            log.info("The credit card {} already exists", dto.getTitle());
            throw new CreditCardAlreadyExistsException();
        }

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
}
