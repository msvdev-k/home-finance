package ru.msvdev.homefinance.data.service;

import lombok.NonNull;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.msvdev.homefinance.component.file.RepositoryFactoryUpdateListener;
import ru.msvdev.homefinance.data.entity.ExpenseEntity;
import ru.msvdev.homefinance.data.exception.NotFoundException;
import ru.msvdev.homefinance.data.repository.ExpenseRepository;

import java.util.List;
import java.util.Optional;


@Service
public class ExpenseService implements RepositoryFactoryUpdateListener {

    private ExpenseRepository expenseRepository;


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


    @Override
    public void repositoryFactoryUpdate(RepositoryFactorySupport repositoryFactorySupport) {
        expenseRepository = repositoryFactorySupport.getRepository(ExpenseRepository.class);
    }
}
