package com.victorprado.financeappbackendjava.entrypoint.controller;

import com.victorprado.financeappbackendjava.service.CreditCardService;
import com.victorprado.financeappbackendjava.service.InvoiceService;
import com.victorprado.financeappbackendjava.service.dto.CreditCardDTO;
import com.victorprado.financeappbackendjava.service.dto.InvoiceCriteria;
import com.victorprado.financeappbackendjava.service.dto.InvoiceDTO;
import com.victorprado.financeappbackendjava.service.dto.UpdateCreditCardDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/credit-cards")
@RequiredArgsConstructor
//@Secured({ROLE_USER, ROLE_ADMIN})
public class CreditCardController {

    private final CreditCardService service;
    private final InvoiceService invoiceService;

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

    @DeleteMapping("/{creditCardId}")
    public ResponseEntity<Void> delete(@PathVariable Long creditCardId) {
        log.info("Delete credit card {} request received", creditCardId);
        service.delete(creditCardId);
        log.info("Credit card {} deleted!", creditCardId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{creditCardId}/invoices")
    public ResponseEntity<List<InvoiceDTO>> getInvoices(@PathVariable Long creditCardId, InvoiceCriteria criteria) {
        log.info("Get invoices request received");
        criteria.setCreditCardId(creditCardId);
        return ResponseEntity.ok(invoiceService.getInvoices(criteria));
    }
}
