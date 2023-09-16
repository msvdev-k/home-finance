package ru.msvdev.homefinance.task.base;

import lombok.Getter;

/**
 * Класс, описывающий все исключения возникающие при выполнении задач
 */
@Getter
public class TaskException extends RuntimeException {

    private final String title;
    private final String header;

    public TaskException(Throwable cause) {
        this("", cause);
    }

    public TaskException(String message, Throwable cause) {
        this("Ошибка выполнения задачи", message, cause);
    }

    public TaskException(String header, String message, Throwable cause) {
        this("Ошибка", header, message, cause);
    }

    public TaskException(String title, String header, String message, Throwable cause) {
        super(message, cause);
        this.title = title;
        this.header = header;
    }
}
