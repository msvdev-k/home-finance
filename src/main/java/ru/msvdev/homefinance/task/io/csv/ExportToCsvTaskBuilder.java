package ru.msvdev.homefinance.task.io.csv;

import org.springframework.context.ApplicationContext;
import ru.msvdev.desktop.utils.task.BaseTaskBuilder;
import ru.msvdev.desktop.utils.task.DataTaskBase;
import ru.msvdev.desktop.utils.task.TaskCancel;
import ru.msvdev.homefinance.data.entity.ExpenseEntity;
import ru.msvdev.homefinance.data.service.ExpenseService;
import ru.msvdev.homefinance.io.csv.CsvIO;
import ru.msvdev.homefinance.io.csv.OperationCsvRow;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;


public class ExportToCsvTaskBuilder extends BaseTaskBuilder<Void> {

    private final ExpenseService expenseService;

    private final CsvIO csvIO;
    private Path filePath;

    public ExportToCsvTaskBuilder(ApplicationContext ctx) {
        super(ctx);
        expenseService = ctx.getBean(ExpenseService.class);
        csvIO = new CsvIO();
    }

    /**
     * Установить значение пути к файлу *.csv
     *
     * @param filePath путь к CSV файлу
     */
    public void setFilePath(Path filePath) {
        this.filePath = filePath;
    }

    public TaskCancel buildAndRun() {
        return buildAndRun(new ImportFromCsvTask());
    }


    /**
     * Класс задачи экспорта данных в CSV файл
     */
    private class ImportFromCsvTask extends DataTaskBase<Void> {

        @Override
        protected Void call() throws Exception {
            List<OperationCsvRow> rowsToCsv = expenseService
                    .findAll()
                    .stream()
                    .map(entity -> {
                        OperationCsvRow row = new OperationCsvRow();

                        row.setDate(entity.getDate());
                        row.setCategory(entity.getCategory().getName());
                        row.setCost(entity.getCost());
                        row.setNote(entity.getNote());
                        row.setCheck(entity.getState() != null && entity.getState() == ExpenseEntity.State.APPROVED);

                        return row;
                    })
                    .collect(Collectors.toList());

            csvIO.exportToCsv(filePath, rowsToCsv);

            return null;
        }
    }
}
