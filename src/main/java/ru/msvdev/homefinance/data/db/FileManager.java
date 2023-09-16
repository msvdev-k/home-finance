package ru.msvdev.homefinance.data.db;

import java.nio.file.Path;

/**
 * Менеджер управления файлом с БД
 */
public interface FileManager {

    /**
     * Открыть файл
     *
     * @param path путь к файлу
     */
    void openFile(Path path);

    /**
     * Закрыть файл
     */
    void closeFile();
}
