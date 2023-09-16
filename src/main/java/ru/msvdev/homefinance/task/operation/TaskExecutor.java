package ru.msvdev.homefinance.task.operation;

import org.springframework.stereotype.Component;
import ru.msvdev.homefinance.task.base.task.DataTaskBase;
import ru.msvdev.homefinance.task.base.task.TaskBase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Component
public class TaskExecutor {

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final ExecutorService dataExecutorService = Executors.newSingleThreadExecutor();

    /**
     * Остановить все выполняемые задачи
     * и завершить все потоки
     */
    public void shutdownNow() {
        executorService.shutdownNow();
        dataExecutorService.shutdownNow();
    }

    /**
     * Выполнить задачу чтения/записи данных в отдельном потоке.
     * Все задачи выполняются в одном потоке в порядке
     * добавления в очередь. (Задачи гарантированно
     * выполняются последовательно)
     */
    public void executeDataTask(DataTaskBase<?> task) {
        dataExecutorService.execute(task);
    }

    /**
     * Выполнить задачу в отдельном потоке.
     * Все задачи выполняются параллельно
     */
    public void executeTask(TaskBase<?> task) {
        executorService.execute(task);
    }
}
