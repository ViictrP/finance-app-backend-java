package com.victorprado.financeappbackendjava.entrypoint.controller;

import com.victorprado.financeappbackendjava.service.CreditCardService;
import com.victorprado.financeappbackendjava.service.dto.CreditCardDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import static com.victorprado.financeappbackendjava.domain.roles.Roles.ROLE_ADMIN;
import static com.victorprado.financeappbackendjava.domain.roles.Roles.ROLE_USER;

@Slf4j
@RestController
@RequestMapping("/v1/credit-cards")
@RequiredArgsConstructor
@CrossOrigin("*")
@Secured({ROLE_USER, ROLE_ADMIN})
public class CreditCardController {

    private final CreditCardService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public CreditCardDTO create(@Valid @RequestBody CreditCardDTO dto) {
        log.info("Create credit card {} request received", dto.getTitle());
        return service.create(dto);
    }
}
