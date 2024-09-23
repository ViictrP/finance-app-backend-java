package com.victor.financeapp.backend.controller;

import com.victor.financeapp.backend.service.RecurringExpenseService;
import com.victor.financeapp.backend.service.dto.RecurringExpenseDTO;
import com.victor.financeapp.backend.service.dto.UpdateRecurringExpenseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/v1/recurring-expenses")
@RequiredArgsConstructor
public class RecurringExpenseController {

    private final RecurringExpenseService service;

    @PostMapping
    public ResponseEntity<RecurringExpenseDTO> create(@Valid @RequestBody RecurringExpenseDTO dto) {
        log.info("Create recurring expense {} request received", dto.getDescription());
        var created = service.create(dto);
        log.info("Recurring expense {} created!", dto.getDescription());
        return ResponseEntity.created(URI.create("/v1/recurring-expenses/" + created.getId()))
                .body(created);
    }

    @PutMapping("/{recurringExpenseId}")
    public ResponseEntity<RecurringExpenseDTO> update(@PathVariable Long recurringExpenseId, @RequestBody UpdateRecurringExpenseDTO dto) {
        log.info("Update recurring expense {} request received", dto.getDescription());
        var updated = service.update(recurringExpenseId, dto);
        log.info("Recurring expense {} updated!", dto.getDescription());
        return ResponseEntity.created(URI.create("/v1/recurring-expenses/" + updated.getId()))
                .body(updated);
    }

    @DeleteMapping("/{recurringExpenseId}")
    public ResponseEntity<RecurringExpenseDTO> delete(@PathVariable Long recurringExpenseId) {
        log.info("Delete recurring expense request received");
        service.delete(recurringExpenseId);
        log.info("Recurring expense deleted!");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
