package ru.msvdev.homefinance.data.db;

import org.springframework.data.repository.core.support.RepositoryFactorySupport;

public interface RepositoryFactoryUpdateListener {

    /**
     * Обновление сведений о репозиториях
     *
     * @param repositoryFactorySupport фабрика для создания репозиториев на основе интерфейсов
     */
    void repositoryFactoryUpdate(RepositoryFactorySupport repositoryFactorySupport);
}
