package ru.msvdev.homefinance.data.exception;

public class NotFoundException extends RuntimeException {

    private static final String NOT_FOUND_EXCEPTION_STRING = "Сущность %s с ID = %d не найдена";

    /**
     * Exception с шаблонным сообщением "Сущность %s с ID = %d не найдена"
     * @param entityName название сущности (%s)
     * @param entityId идентификатор сущности (%d)
     * @return NotFoundException с шаблонным сообщением
     */
    public static NotFoundException getInstance(String entityName, Integer entityId) {
        return new NotFoundException(String.format(
                NOT_FOUND_EXCEPTION_STRING, entityName, entityId
        ));
    }

    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }
}
