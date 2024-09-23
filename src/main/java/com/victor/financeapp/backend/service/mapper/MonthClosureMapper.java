package com.victor.financeapp.backend.service.mapper;

import com.victor.financeapp.backend.domain.entity.MonthClosure;
import com.victor.financeapp.backend.service.dto.MonthClosureDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MonthClosureMapper extends BaseMapper<MonthClosureDTO, MonthClosure> {
}
