package ru.msvdev.homefinance.data.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.msvdev.desktop.utils.task.TaskException;
import ru.msvdev.homefinance.data.entity.CategoryEntity;
import ru.msvdev.homefinance.data.exception.NotFoundException;
import ru.msvdev.homefinance.data.repository.CategoryRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;


    @Transactional(readOnly = true)
    public List<CategoryEntity> findAll() {
        return categoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<CategoryEntity> findById(@NonNull Integer id) {
        return categoryRepository.findById(id);
    }

    @Transactional
    public CategoryEntity newCategory(@NonNull CategoryEntity categoryEntity) {
        categoryEntity.setId(null);
        return categoryRepository.save(categoryEntity);
    }

    @Transactional
    public List<CategoryEntity> newCategory(@NonNull Collection<CategoryEntity> categoryEntities) {
        categoryEntities.forEach(entity -> entity.setId(null));
        return categoryRepository.saveAll(categoryEntities);
    }

    @Transactional
    public CategoryEntity updateCategory(@NonNull CategoryEntity categoryEntity) {
        Optional<CategoryEntity> optionalCategoryEntity = findById(categoryEntity.getId());
        if (!optionalCategoryEntity.isPresent()) {
            throw NotFoundException.getInstance("CategoryEntity", categoryEntity.getId());
        }

        return categoryRepository.save(categoryEntity);
    }

    @Transactional
    public void deleteById(@NonNull Integer id) {
        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(id);
        if (!categoryEntity.isPresent()) return;

        if (categoryEntity.get().getExpenses().isEmpty()) {
            categoryRepository.deleteById(id);

        } else {
            throw new TaskException(
                    TaskException.Type.WARNING,
                    "Нельзя удалить категорию на которую ссылаются записи о расходах!",
                    null
            );
        }
    }

}
