package com.victorprado.financeapp.entrypoint.mapper;

import com.victorprado.financeapp.core.entities.Transaction;
import com.victorprado.financeapp.entrypoint.request.TransactionRequest;
import com.victorprado.financeapp.entrypoint.response.TransactionResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionEntityResponseMapper extends
  EntityResponseMapper<Transaction, TransactionResponse, TransactionRequest> {

}
