package com.victorprado.financeappbackendjava.service;

import com.victorprado.financeappbackendjava.domain.repository.CreditCardRepository;
import com.victorprado.financeappbackendjava.entrypoint.controller.context.SecurityContext;
import com.victorprado.financeappbackendjava.service.dto.CreditCardDTO;
import com.victorprado.financeappbackendjava.service.mapper.CreditCardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreditCardService {

    private final CreditCardRepository repository;
    private final CreditCardMapper mapper;

    public CreditCardDTO create(CreditCardDTO dto) {
        log.info("Converting the credit card {} information", dto.getTitle());
        var entity = mapper.toEntity(dto);
        var userId = SecurityContext.getUserId();
        entity.setUserId(userId);
        log.info("Saving the credit card {} information", dto.getTitle());
        repository.save(entity);
        log.info("The credit card {} is created.", dto.getTitle());
        return mapper.toDTO(entity);
    }
}
