package com.victorprado.financeappbackendjava.service.mapper;

import com.victorprado.financeappbackendjava.domain.entity.Transaction;
import com.victorprado.financeappbackendjava.service.dto.TransactionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper extends BaseMapper<TransactionDTO, Transaction> {

    @Mapping(target = "userId", source = "user.id")
    TransactionDTO toDTO(Transaction transaction);
}
