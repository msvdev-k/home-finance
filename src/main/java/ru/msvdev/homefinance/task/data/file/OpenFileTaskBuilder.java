package ru.msvdev.homefinance.task.data.file;

import org.springframework.context.ApplicationContext;
import ru.msvdev.homefinance.data.db.FileManager;
import ru.msvdev.homefinance.task.base.BaseTaskBuilder;
import ru.msvdev.homefinance.task.base.task.DataTaskBase;
import ru.msvdev.homefinance.task.operation.TaskCancel;

import java.nio.file.Files;
import java.nio.file.Path;


public class OpenFileTaskBuilder extends BaseTaskBuilder<Void> {

    private final FileManager fileManager;

    private Path filePath;
    private boolean newFile;

    public OpenFileTaskBuilder(ApplicationContext ctx) {
        super(ctx);
        fileManager = ctx.getBean(FileManager.class);
    }

    public TaskCancel buildAndRun() {
        if (filePath == null) {
            throw new NullPointerException("filePath - обязательный параметр не равный null");
        }

        return buildAndRun(new OpenFileTask());
    }

    /**
     * Установить значение пути к файлу
     *
     * @param filePath путь к открываемому файлу
     */
    public void setFilePath(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Установить параметр открытия файла
     *
     * @param newFile true - создать файл, false - открыть существующий файл
     */
    public void setNewFile(boolean newFile) {
        this.newFile = newFile;
    }


    /**
     * Класс задачи открытия файла БД
     */
    private class OpenFileTask extends DataTaskBase<Void> {

        @Override
        protected Void call() throws Exception {
            if (newFile && Files.exists(filePath)) {
                Files.delete(filePath);
            }
            fileManager.openFile(filePath);
            return null;
        }
    }
}
