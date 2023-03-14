package com.victorprado.financeappbackendjava.service.mapper;

import com.victorprado.financeappbackendjava.domain.entity.RecurringExpense;
import com.victorprado.financeappbackendjava.service.dto.RecurringExpenseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RecurringExpenseMapper extends BaseMapper<RecurringExpenseDTO, RecurringExpense> {

}
