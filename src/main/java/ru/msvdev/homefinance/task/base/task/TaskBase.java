package ru.msvdev.homefinance.task.base.task;

import javafx.concurrent.Task;
import ru.msvdev.homefinance.task.base.TaskException;

import java.util.Set;
import java.util.function.Consumer;


/**
 * Базовый класс для всех задач.
 * Все классы задач должны быть унаследованы от этого базового класса
 *
 * @param <T> тип результата, возвращаемого в случае успешного завершения задачи
 */
public abstract class TaskBase<T> extends Task<T> {

    private Set<Consumer<Boolean>> runningListeners;
    private Set<Consumer<T>> succeededListeners;
    private Set<Consumer<TaskException>> failedListeners;
    private Set<Runnable> cancelledListeners;

    private Set<Consumer<Double>> progressListeners;
    private Set<Consumer<String>> messageListeners;


    public TaskBase() {
        progressProperty().addListener((observable, oldValue, newValue) ->
                progressListeners.forEach(doubleConsumer -> doubleConsumer.accept((Double) newValue)));

        messageProperty().addListener((observable, oldValue, newValue) ->
                messageListeners.forEach(stringConsumer -> stringConsumer.accept(newValue)));
    }


    public void setRunningListeners(Set<Consumer<Boolean>> runningListeners) {
        this.runningListeners = runningListeners;
    }

    public void setSucceededListeners(Set<Consumer<T>> succeededListeners) {
        this.succeededListeners = succeededListeners;
    }

    public void setFailedListeners(Set<Consumer<TaskException>> failedListeners) {
        this.failedListeners = failedListeners;
    }

    public void setCancelledListeners(Set<Runnable> cancelledListeners) {
        this.cancelledListeners = cancelledListeners;
    }

    public void setProgressListeners(Set<Consumer<Double>> progressListeners) {
        this.progressListeners = progressListeners;
    }

    public void setMessageListeners(Set<Consumer<String>> messageListeners) {
        this.messageListeners = messageListeners;
    }


    @Override
    protected void succeeded() {
        super.succeeded();
        runningListeners.forEach(booleanConsumer -> booleanConsumer.accept(false));

        T value = getValue();
        succeededListeners.forEach(tConsumer -> tConsumer.accept(value));
    }

    @Override
    protected void cancelled() {
        super.cancelled();
        runningListeners.forEach(booleanConsumer -> booleanConsumer.accept(false));
        cancelledListeners.forEach(Runnable::run);
    }

    @Override
    protected void failed() {
        super.failed();
        runningListeners.forEach(booleanConsumer -> booleanConsumer.accept(false));

        TaskException taskException;
        Throwable e = getException();

        if (e instanceof TaskException) {
            taskException = (TaskException) e;
        } else {
            taskException = new TaskException(e);
        }

        failedListeners.forEach(taskExceptionConsumer -> taskExceptionConsumer.accept(taskException));
    }
}
