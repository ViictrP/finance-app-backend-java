package com.victor.financeapp.backend.service.mapper;

import com.victor.financeapp.backend.domain.entity.RecurringExpense;
import com.victor.financeapp.backend.service.dto.RecurringExpenseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RecurringExpenseMapper extends BaseMapper<RecurringExpenseDTO, RecurringExpense> {

    @Mapping(target = "userId", source = "user.id")
    RecurringExpenseDTO toDTO(RecurringExpense recurringExpense);
}
