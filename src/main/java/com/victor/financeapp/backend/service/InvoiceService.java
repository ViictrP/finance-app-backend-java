package com.victor.financeapp.backend.service;

import com.victor.financeapp.backend.domain.exception.UserNotFoundException;
import com.victor.financeapp.backend.domain.repository.CreditCardRepository;
import com.victor.financeapp.backend.domain.repository.InvoiceRepository;
import com.victor.financeapp.backend.domain.repository.UserRepository;
import com.victor.financeapp.backend.service.context.SecurityContext;
import com.victor.financeapp.backend.service.dto.InvoiceCriteria;
import com.victor.financeapp.backend.service.dto.InvoiceDTO;
import com.victor.financeapp.backend.service.mapper.InvoiceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository repository;
    private final CreditCardRepository creditCardRepository;
    private final UserRepository userRepository;
    private final InvoiceMapper mapper;

    public List<InvoiceDTO> getInvoices(InvoiceCriteria criteria) {
        log.info("Loading logged user's {} data", SecurityContext.getUserEmail());
        var user = userRepository.findByEmail(SecurityContext.getUserEmail())
                .orElseThrow(UserNotFoundException::new);

        log.info("Loading credit card's {} data", criteria.getCreditCardId());
        var creditCard = creditCardRepository.findById(criteria.getCreditCardId())
                .orElseThrow(UserNotFoundException::new);

        return mapper.toDTO(repository.findByCreditCardAndMonthAndYearAndCreditCard_User(creditCard, criteria.getMonth(), criteria.getYear(), user));
    }
}
