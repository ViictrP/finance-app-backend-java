package com.victorprado.financeapp.entrypoint.mapper;

import com.victorprado.financeapp.core.entities.CreditCard;
import com.victorprado.financeapp.entrypoint.request.CreditCardRequest;
import com.victorprado.financeapp.entrypoint.response.CreditCardResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreditCardEntityResponseMapper extends
  EntityResponseMapper<CreditCard, CreditCardResponse, CreditCardRequest> {

}
