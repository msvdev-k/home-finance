package ru.msvdev.homefinance.task.base;

import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import ru.msvdev.homefinance.config.AppConstant;
import ru.msvdev.homefinance.task.base.task.DataTaskBase;
import ru.msvdev.homefinance.task.base.task.TaskBase;
import ru.msvdev.homefinance.task.operation.TaskCancel;
import ru.msvdev.homefinance.task.operation.TaskExecutor;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;


/**
 * Базовый класс для всех построителей задач.
 * Все классы построителей должны быть унаследованы от этого базового класса
 *
 * @param <T> тип результата, возвращаемого в случае успешного завершения задачи
 */
public abstract class BaseTaskBuilder<T> {
    static final Logger logger = LoggerFactory.getLogger(AppConstant.LOGGER_NAME);

    protected final ApplicationContext ctx;

    private final Set<Consumer<Boolean>> runningListeners = new HashSet<>();
    private final Set<Consumer<T>> succeededListeners = new HashSet<>();
    private final Set<Consumer<TaskException>> failedListeners = new HashSet<>();
    private final Set<Runnable> cancelledListeners = new HashSet<>();

    private final Set<Consumer<Double>> progressListeners = new HashSet<>();
    private final Set<Consumer<String>> messageListeners = new HashSet<>();


    public BaseTaskBuilder(ApplicationContext ctx) {
        this.ctx = ctx;

        failedListeners.add(this::loggerFailedListener);
        failedListeners.add(this::alertFailedListener);
    }


    public void addRunningListener(Consumer<Boolean> runningListener) {
        runningListeners.add(runningListener);
    }

    public void removeRunningListener(Consumer<Boolean> runningListener) {
        runningListeners.remove(runningListener);
    }

    public void addSucceededListener(Consumer<T> succeededListener) {
        succeededListeners.add(succeededListener);
    }

    public void removeSucceededListener(Consumer<T> succeededListener) {
        succeededListeners.remove(succeededListener);
    }

    public void addFailedListener(Consumer<TaskException> failedListener) {
        failedListeners.add(failedListener);
    }

    public void removeFailedListener(Consumer<TaskException> failedListener) {
        failedListeners.remove(failedListener);
    }

    public void addCancelledListener(Runnable cancelledListener) {
        cancelledListeners.add(cancelledListener);
    }

    public void removeCancelledListener(Runnable cancelledListener) {
        cancelledListeners.remove(cancelledListener);
    }

    public void addProgressListener(Consumer<Double> progressListener) {
        progressListeners.add(progressListener);
    }

    public void removeProgressListener(Consumer<Double> progressListener) {
        progressListeners.remove(progressListener);
    }

    public void addMessageListener(Consumer<String> messageListener) {
        messageListeners.add(messageListener);
    }

    public void removeMessageListener(Consumer<String> messageListener) {
        messageListeners.remove(messageListener);
    }


    protected TaskCancel buildAndRun(TaskBase<T> task) {
        task.setRunningListeners(runningListeners);
        task.setSucceededListeners(succeededListeners);
        task.setCancelledListeners(cancelledListeners);
        task.setFailedListeners(failedListeners);
        task.setProgressListeners(progressListeners);
        task.setMessageListeners(messageListeners);

        TaskExecutor taskExecutor = ctx.getBean(TaskExecutor.class);

        runningListeners.forEach(booleanConsumer -> booleanConsumer.accept(true));

        if (task instanceof DataTaskBase) {
            taskExecutor.executeDataTask((DataTaskBase<T>) task);
        } else {
            taskExecutor.executeTask(task);
        }

        return task::cancel;
    }


    private void loggerFailedListener(TaskException taskException) {
        switch (taskException.getType()) {
            case INFORMATION:
                logger.info(taskException.getMessage());
                break;

            case WARNING:
                logger.warn(taskException.getMessage());
                break;

            case ERROR:
                logger.error(taskException.getMessage(), taskException.getCause());
                break;
        }
    }

    private void alertFailedListener(TaskException taskException) {
        Alert alert = null;

        switch (taskException.getType()) {
            case INFORMATION:
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Информация");
                break;

            case WARNING:
                alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Предупреждение");
                break;

            case ERROR:
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                break;
        }

        alert.setHeaderText(null);

        String message = taskException.getMessage();
        if (message == null || message.trim().isEmpty()) {
            message = "Сообщение отсутствует ¯\\_(ツ)_/¯";
        }
        alert.setContentText(message);
        alert.showAndWait();
    }
}
