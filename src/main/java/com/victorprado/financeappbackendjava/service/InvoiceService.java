package com.victorprado.financeappbackendjava.service;

import com.victorprado.financeappbackendjava.domain.exception.UserNotFoundException;
import com.victorprado.financeappbackendjava.domain.repository.CreditCardRepository;
import com.victorprado.financeappbackendjava.domain.repository.InvoiceRepository;
import com.victorprado.financeappbackendjava.domain.repository.UserRepository;
import com.victorprado.financeappbackendjava.entrypoint.controller.context.SecurityContext;
import com.victorprado.financeappbackendjava.service.dto.InvoiceCriteria;
import com.victorprado.financeappbackendjava.service.dto.InvoiceDTO;
import com.victorprado.financeappbackendjava.service.mapper.InvoiceMapper;
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
