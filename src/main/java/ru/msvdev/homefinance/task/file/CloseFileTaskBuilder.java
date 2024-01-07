package ru.msvdev.homefinance.task.file;

import org.springframework.context.ApplicationContext;
import ru.msvdev.desktop.utils.data.FileManager;
import ru.msvdev.desktop.utils.task.BaseTaskBuilder;
import ru.msvdev.desktop.utils.task.DataTaskBase;
import ru.msvdev.desktop.utils.task.TaskCancel;


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
