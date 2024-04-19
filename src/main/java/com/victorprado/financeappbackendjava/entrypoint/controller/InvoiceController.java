package com.victorprado.financeappbackendjava.entrypoint.controller;

import com.victorprado.financeappbackendjava.service.InvoiceService;
import com.victorprado.financeappbackendjava.service.dto.InvoiceCriteria;
import com.victorprado.financeappbackendjava.service.dto.InvoiceDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/invoices")
@RequiredArgsConstructor
@CrossOrigin("*")
public class InvoiceController {

    private final InvoiceService service;

    @GetMapping
    public ResponseEntity<List<InvoiceDTO>> getInvoices(@Valid InvoiceCriteria criteria) {
        log.info("Get invoices request received");
        return ResponseEntity.ok(service.getInvoices(criteria));
    }
}
