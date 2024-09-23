package com.victor.financeapp.backend.service;

import com.victor.financeapp.backend.domain.exception.RecurringExpenseNotFoundException;
import com.victor.financeapp.backend.domain.exception.UserNotFoundException;
import com.victor.financeapp.backend.domain.repository.RecurringExpenseRepository;
import com.victor.financeapp.backend.domain.repository.UserRepository;
import com.victor.financeapp.backend.service.context.SecurityContext;
import com.victor.financeapp.backend.service.dto.RecurringExpenseDTO;
import com.victor.financeapp.backend.service.dto.UpdateRecurringExpenseDTO;
import com.victor.financeapp.backend.service.mapper.RecurringExpenseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecurringExpenseService {

    private final RecurringExpenseRepository repository;
    private final UserRepository userRepository;
    private final RecurringExpenseMapper mapper;

    public RecurringExpenseDTO create(RecurringExpenseDTO dto) {
        log.info("Loading the logged user {} information", SecurityContext.getUserEmail());
        var user = userRepository.findByEmail(SecurityContext.getUserEmail())
                .orElseThrow(UserNotFoundException::new);

        var entity = mapper.toEntity(dto);
        entity.setUser(user);
        log.info("Creating transaction {}", dto.getDescription());
        return mapper.toDTO(repository.save(entity));
    }

    public RecurringExpenseDTO update(Long recurringExpenseId, UpdateRecurringExpenseDTO dto) {
        log.info("Loading the recurring expense {} information", recurringExpenseId);
        var recurringExpense = repository.findById(recurringExpenseId)
                .orElseThrow(RecurringExpenseNotFoundException::new);

        recurringExpense.setDescription(dto.getDescription());
        recurringExpense.setAmount(dto.getAmount());
        recurringExpense.setCategory(dto.getCategory());

        log.info("Saving the recurring expense {} information", recurringExpenseId);
        return mapper.toDTO(repository.save(recurringExpense));
    }

    public void delete(Long recurringExpenseId) {
        log.info("Loading the recurring expense {} information", recurringExpenseId);
        var recurringExpense = repository.findById(recurringExpenseId)
                .orElseThrow(RecurringExpenseNotFoundException::new);

        recurringExpense.setDeleted(true);
        log.info("Deleting the recurring expense {}", recurringExpense.getDescription());
        repository.save(recurringExpense);
    }
}
