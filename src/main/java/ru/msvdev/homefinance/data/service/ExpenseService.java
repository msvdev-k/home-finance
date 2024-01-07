package ru.msvdev.homefinance.data.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.msvdev.homefinance.data.entity.ExpenseEntity;
import ru.msvdev.homefinance.data.exception.NotFoundException;
import ru.msvdev.homefinance.data.repository.ExpenseRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;


    @Transactional(readOnly = true)
    public List<ExpenseEntity> findAll() {
        return expenseRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<ExpenseEntity> findById(@NonNull Integer id) {
        return expenseRepository.findById(id);
    }

    @Transactional
    public ExpenseEntity newExpense(@NonNull ExpenseEntity expenseEntity) {
        expenseEntity.setId(null);
        return expenseRepository.save(expenseEntity);
    }

    @Transactional
    public List<ExpenseEntity> newExpense(@NonNull Collection<ExpenseEntity> expenseEntities) {
        expenseEntities.forEach(expenseEntity -> expenseEntity.setId(null));
        return expenseRepository.saveAll(expenseEntities);
    }

    @Transactional
    public ExpenseEntity updateExpense(@NonNull ExpenseEntity expenseEntity) {
        Optional<ExpenseEntity> optionalExpenseEntity = findById(expenseEntity.getId());
        if (!optionalExpenseEntity.isPresent()) {
            throw NotFoundException.getInstance("ExpenseEntity", expenseEntity.getId());
        }

        return expenseRepository.save(expenseEntity);
    }

    @Transactional
    public void deleteById(@NonNull Integer id) {
        expenseRepository.deleteById(id);
    }

    @Transactional
    public void deleteById(@NonNull Collection<Integer> idCollection) {
        expenseRepository.deleteAllById(idCollection);
    }

}
