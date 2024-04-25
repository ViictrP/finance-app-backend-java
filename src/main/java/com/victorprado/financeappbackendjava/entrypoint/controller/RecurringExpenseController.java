package com.victorprado.financeappbackendjava.entrypoint.controller;

import com.victorprado.financeappbackendjava.service.RecurringExpenseService;
import com.victorprado.financeappbackendjava.service.dto.RecurringExpenseDTO;
import com.victorprado.financeappbackendjava.service.dto.UpdateRecurringExpenseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/v1/recurring-expenses")
@RequiredArgsConstructor
@CrossOrigin("*")
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
