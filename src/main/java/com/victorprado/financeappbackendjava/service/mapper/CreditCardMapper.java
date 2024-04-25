package com.victorprado.financeappbackendjava.service.mapper;

import com.victorprado.financeappbackendjava.domain.entity.CreditCard;
import com.victorprado.financeappbackendjava.service.dto.CreditCardDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = InvoiceMapper.class)
public interface CreditCardMapper extends BaseMapper<CreditCardDTO, CreditCard> {

    @Mapping(target = "invoices", ignore = true)
    CreditCardDTO toDTOWithoutInvoices(CreditCard creditCard);
}
