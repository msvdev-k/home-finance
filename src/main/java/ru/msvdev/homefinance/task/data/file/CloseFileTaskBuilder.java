package ru.msvdev.homefinance.task.data.file;

import org.springframework.context.ApplicationContext;
import ru.msvdev.homefinance.data.db.FileManager;
import ru.msvdev.homefinance.task.base.BaseTaskBuilder;
import ru.msvdev.homefinance.task.base.task.DataTaskBase;
import ru.msvdev.homefinance.task.operation.TaskCancel;


public class CloseFileTaskBuilder extends BaseTaskBuilder<Void> {

    private final FileManager fileManager;

    public CloseFileTaskBuilder(ApplicationContext ctx) {
        super(ctx);
        fileManager = ctx.getBean(FileManager.class);
    }

    public TaskCancel buildAndRun() {
        return buildAndRun(new CloseFileTask());
    }


    /**
     * Класс задачи закрытия файла БД
     */
    private class CloseFileTask extends DataTaskBase<Void> {

        @Override
        protected Void call() throws Exception {
            fileManager.closeFile();
            return null;
        }
    }
}
