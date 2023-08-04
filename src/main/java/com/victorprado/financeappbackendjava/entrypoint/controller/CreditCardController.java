package com.victorprado.financeappbackendjava.entrypoint.controller;

import com.victorprado.financeappbackendjava.service.CreditCardService;
import com.victorprado.financeappbackendjava.service.dto.CreditCardDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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
    public ResponseEntity<CreditCardDTO> create(@Valid @RequestBody CreditCardDTO dto) {
        log.info("Create credit card {} request received", dto.getTitle());
        var created = service.create(dto);
        return ResponseEntity.created(URI.create("/v1/credit-cards/" + created.getId()))
                .body(created);
    }
}