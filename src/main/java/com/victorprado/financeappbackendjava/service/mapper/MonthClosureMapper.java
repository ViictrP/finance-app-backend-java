package com.victorprado.financeappbackendjava.service.mapper;

import com.victorprado.financeappbackendjava.domain.entity.MonthClosure;
import com.victorprado.financeappbackendjava.service.dto.MonthClosureDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MonthClosureMapper extends BaseMapper<MonthClosureDTO, MonthClosure> {
}
