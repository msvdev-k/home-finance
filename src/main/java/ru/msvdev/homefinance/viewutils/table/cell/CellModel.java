package ru.msvdev.homefinance.viewutils.table.cell;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public abstract class CellModel<T> {

    private T value;

    private CellWarning warning;
    private CellError error;

    {
        value = null;
        warning = null;
        error = null;
    }


    public void setValue(T value) {
        this.value = value;
        this.warning = CellWarning.NO_SYNC;
    }

    public void resetWarning() {
        warning = null;
    }

    public boolean isWarning() {
        return warning != null;
    }

    public String getWarningDescription() {
        return warning.getWarningDescription();
    }

    public void resetError() {
        this.error = null;
    }

    public boolean isError() {
        return error != null;
    }

    public String getErrorText() {
        return error.getErrorText();
    }

    public String getErrorDescription() {
        return error.getErrorDescription();
    }


    @Getter
    public enum CellError {

        ERROR("ОШИБКА", "Неизвестна ошибка"),
        REQUIRED("*", "Обязательное значение"),
        NUMBER("ЧИСЛО", "Недопустимое числовое значение"),
        NAME("ИМЯ", "Недопустимое строковое значение"),
        DATE("ДАТА", "Недопустимое значение даты"),
        UNIQUE("УНИК", "Значение должно быть уникальным в пределах текущего столбца"),
        EMPTY("ПУСТО", "Строка не может быть пустой или состоять из пробельных символов");

        private final String errorText;
        private final String errorDescription;

        CellError(String errorText, String errorDescription) {
            this.errorText = errorText;
            this.errorDescription = errorDescription;
        }
    }


    @Getter
    public enum CellWarning {

        NO_SYNC("Значение не синхронизовано с БД");

        private final String warningDescription;

        CellWarning(String warningDescription) {
            this.warningDescription = warningDescription;
        }
    }
}
