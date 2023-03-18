package com.victorprado.financeappbackendjava.service.mapper;

import com.victorprado.financeappbackendjava.domain.entity.Salary;
import com.victorprado.financeappbackendjava.service.dto.SalaryDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SalaryMapper extends BaseMapper<SalaryDTO, Salary> {

}
