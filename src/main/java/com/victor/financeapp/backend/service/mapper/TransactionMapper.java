package com.victor.financeapp.backend.service.mapper;

import com.victor.financeapp.backend.domain.entity.Transaction;
import com.victor.financeapp.backend.service.dto.TransactionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper extends BaseMapper<TransactionDTO, Transaction> {

    @Mapping(target = "userId", source = "user.id")
    TransactionDTO toDTO(Transaction transaction);
}
