package ru.msvdev.homefinance.task.file;

import org.springframework.context.ApplicationContext;
import ru.msvdev.desktop.utils.data.FileManager;
import ru.msvdev.desktop.utils.task.BaseTaskBuilder;
import ru.msvdev.desktop.utils.task.DataTaskBase;
import ru.msvdev.desktop.utils.task.TaskCancel;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;


public class OpenFileTaskBuilder extends BaseTaskBuilder<Void> {

    private final FileManager fileManager;

    private Path filePath;
    private boolean newFile;
    private boolean backup;

    public OpenFileTaskBuilder(ApplicationContext ctx) {
        super(ctx);
        fileManager = ctx.getBean(FileManager.class);
        backup = true;
    }

    public TaskCancel buildAndRun() {
        if (filePath == null) {
            throw new NullPointerException("filePath - обязательный параметр не равный null");
        }

        addFailedListener(taskException -> fileManager.closeFile());
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
     * Параметр, отвечающий за создание резервной копии
     * файла перед его открытием
     *
     * @param backup true - создать резервною копию (значение по умолчанию), false - копию не делать
     */
    public void setBackup(boolean backup) {
        this.backup = backup;
    }

    /**
     * Класс задачи открытия файла БД
     */
    private class OpenFileTask extends DataTaskBase<Void> {

        private static final String BACKUP_FILE_TEMPLATE = "%1$s.%2$tFT%2$tH-%2$tM-%2$tS.bak";

        @Override
        protected Void call() throws Exception {
            if (Files.exists(filePath)) {
                if (newFile) {
                    Files.delete(filePath);

                } else if (backup) {
                    LocalDateTime dateTime = LocalDateTime.now();
                    Path backupPath = Paths.get(String.format(BACKUP_FILE_TEMPLATE, filePath, dateTime)).normalize();
                    Files.copy(filePath, backupPath);
                }
            }

            fileManager.openFile(filePath);
            return null;
        }
    }
}
