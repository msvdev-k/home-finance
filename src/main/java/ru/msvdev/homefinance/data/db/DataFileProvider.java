package ru.msvdev.homefinance.data.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.SharedEntityManagerCreator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.nio.file.Path;
import java.util.List;


@Component
@RequiredArgsConstructor
public class DataFileProvider implements FileManager, PlatformTransactionManager {

    private static final String DB_URL_TEMPLATE = "jdbc:sqlite:%s?DATE_CLASS=text";

    private final List<RepositoryFactoryUpdateListener> repositoryFactoryUpdateListeners;

    private HikariDataSource dataSource;
    private EntityManagerFactory entityManagerFactory;
    private PlatformTransactionManager transactionManager;
    private EntityManager entityManager;
    private RepositoryFactorySupport repositoryFactory;


    @Override
    public void openFile(Path path) {
        closeFile();
        connectToDB(path.toString());
        migrate();
        configureDataAccess();
    }

    @Override
    public void closeFile() {
        repositoryFactory = null;

        if (entityManager != null) {
            entityManager.close();
            entityManager = null;
        }

        transactionManager = null;

        if (entityManagerFactory != null) {
            entityManagerFactory.close();
            entityManagerFactory = null;
        }

        if (dataSource != null) {
            dataSource.close();
            dataSource = null;
        }
    }


    /**
     * Установить соединение с файлом БД
     *
     * @param db путь к файлу БД
     */
    private void connectToDB(String db) {
        String url = String.format(DB_URL_TEMPLATE, db);

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
//        config.setUsername(DB_USERNAME);
//        config.setPassword(DB_PASSWORD);

        dataSource = new HikariDataSource(config);
    }

    /**
     * Актуализировать версию БД
     */
    private void migrate() {
        Flyway
                .configure()
                .dataSource(dataSource)
                .load()
                .migrate();
    }

    /**
     * Сконфигурировать EntityManagerFactory, PlatformTransactionManager
     * и RepositoryFactorySupport
     */
    private void configureDataAccess() {
        Configuration configuration = new Configuration().configure();

        StandardServiceRegistry standardServiceRegistry = configuration
                .getStandardServiceRegistryBuilder()
                .applySetting(Environment.DATASOURCE, dataSource)
                .build();

        entityManagerFactory = configuration.buildSessionFactory(standardServiceRegistry);

        transactionManager = new JpaTransactionManager(entityManagerFactory);

        entityManager = SharedEntityManagerCreator
                .createSharedEntityManager(entityManagerFactory);

        repositoryFactory = new JpaRepositoryFactory(entityManager);
        repositoryFactoryUpdateListeners.forEach(
                listener -> listener.repositoryFactoryUpdate(repositoryFactory)
        );
    }

    @Override
    public TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
        return transactionManager.getTransaction(definition);
    }

    @Override
    public void commit(TransactionStatus status) throws TransactionException {
        transactionManager.commit(status);
    }

    @Override
    public void rollback(TransactionStatus status) throws TransactionException {
        transactionManager.rollback(status);
    }
}
