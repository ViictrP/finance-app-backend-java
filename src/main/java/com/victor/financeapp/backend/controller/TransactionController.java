package com.victor.financeapp.backend.controller;

import com.victor.financeapp.backend.service.TransactionService;
import com.victor.financeapp.backend.service.dto.TransactionDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
