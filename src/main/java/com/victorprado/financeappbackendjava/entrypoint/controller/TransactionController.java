package com.victorprado.financeappbackendjava.entrypoint.controller;

import com.victorprado.financeappbackendjava.service.TransactionService;
import com.victorprado.financeappbackendjava.service.dto.TransactionDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;

    @PostMapping
    public ResponseEntity<TransactionDTO> create(@Valid @RequestBody TransactionDTO dto) {
        log.info("Create transaction {} request received", dto.getDescription());
        var created = service.create(dto);
        log.info("Transaction {} created!", dto.getDescription());
        return ResponseEntity.created(URI.create("/v1/transactions/" + created.getId()))
                .body(created);
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> delete(@PathVariable Long transactionId, @RequestParam("all") Boolean all) {
        log.info("Delete transaction {} request received", transactionId);
        service.delete(transactionId, all);
        log.info("Transaction {} deleted!", transactionId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
