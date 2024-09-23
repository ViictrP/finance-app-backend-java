package com.victor.financeapp.backend.service.mapper;

import com.victor.financeapp.backend.domain.entity.Invoice;
import com.victor.financeapp.backend.service.dto.InvoiceDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InvoiceMapper extends BaseMapper<InvoiceDTO, Invoice> {

}
