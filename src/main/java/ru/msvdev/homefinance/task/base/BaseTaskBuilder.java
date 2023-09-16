package ru.msvdev.homefinance.task.base;

import org.springframework.context.ApplicationContext;
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

    protected final ApplicationContext ctx;

    private final Set<Consumer<Boolean>> runningListeners = new HashSet<>();
    private final Set<Consumer<T>> succeededListeners = new HashSet<>();
    private final Set<Consumer<TaskException>> failedListeners = new HashSet<>();
    private final Set<Runnable> cancelledListeners = new HashSet<>();

    private final Set<Consumer<Double>> progressListeners = new HashSet<>();
    private final Set<Consumer<String>> messageListeners = new HashSet<>();


    public BaseTaskBuilder(ApplicationContext ctx) {
        this.ctx = ctx;
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

}
