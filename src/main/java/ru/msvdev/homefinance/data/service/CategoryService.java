package ru.msvdev.homefinance.data.service;

import lombok.NonNull;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.msvdev.homefinance.data.db.RepositoryFactoryUpdateListener;
import ru.msvdev.homefinance.data.entity.CategoryEntity;
import ru.msvdev.homefinance.data.exception.NotFoundException;
import ru.msvdev.homefinance.data.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;


@Service
public class CategoryService implements RepositoryFactoryUpdateListener {

    private CategoryRepository categoryRepository;


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
    public CategoryEntity updateCategory(@NonNull CategoryEntity categoryEntity) {
        Optional<CategoryEntity> optionalCategoryEntity = findById(categoryEntity.getId());
        if (!optionalCategoryEntity.isPresent()) {
            throw NotFoundException.getInstance("CategoryEntity", categoryEntity.getId());
        }

        return categoryRepository.save(categoryEntity);
    }

    @Transactional
    public void deleteById(@NonNull Integer id) {
        categoryRepository.deleteById(id);
    }


    @Override
    public void repositoryFactoryUpdate(RepositoryFactorySupport repositoryFactorySupport) {
        categoryRepository = repositoryFactorySupport.getRepository(CategoryRepository.class);
    }
}
