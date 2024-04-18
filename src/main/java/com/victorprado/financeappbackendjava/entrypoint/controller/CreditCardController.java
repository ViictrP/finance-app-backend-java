package com.victorprado.financeappbackendjava.entrypoint.controller;

import com.victorprado.financeappbackendjava.service.CreditCardService;
import com.victorprado.financeappbackendjava.service.dto.CreditCardDTO;
import com.victorprado.financeappbackendjava.service.dto.UpdateCreditCardDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
//@Secured({ROLE_USER, ROLE_ADMIN})
public class CreditCardController {

    private final CreditCardService service;

    @PostMapping
    public ResponseEntity<CreditCardDTO> create(@Valid @RequestBody CreditCardDTO dto) {
        log.info("Create credit card {} request received", dto.getTitle());
        var created = service.create(dto);
        log.info("Credit card {} created!", dto.getTitle());
        return ResponseEntity.created(URI.create("/v1/credit-cards/" + created.getId()))
                .body(created);
    }

    @PutMapping("/{creditCardId}")
    public ResponseEntity<CreditCardDTO> update(@PathVariable Long creditCardId, @Valid @RequestBody UpdateCreditCardDTO dto) {
        log.info("Update credit card {} request received", dto.getTitle());
        var created = service.update(creditCardId, dto);
        log.info("Credit card {} updated!", dto.getTitle());
        return ResponseEntity.created(URI.create("/v1/credit-cards/" + created.getId()))
                .body(created);
    }
}
