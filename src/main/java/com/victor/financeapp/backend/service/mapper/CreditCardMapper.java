package com.victor.financeapp.backend.service.mapper;

import com.victor.financeapp.backend.domain.entity.CreditCard;
import com.victor.financeapp.backend.service.dto.CreditCardDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = InvoiceMapper.class)
public interface CreditCardMapper extends BaseMapper<CreditCardDTO, CreditCard> {

    @Mapping(target = "invoices", ignore = true)
    CreditCardDTO toDTOWithoutInvoices(CreditCard creditCard);
}
