package ru.msvdev.homefinance.task.data.io.csv;

import org.springframework.context.ApplicationContext;
import ru.msvdev.homefinance.data.entity.CategoryEntity;
import ru.msvdev.homefinance.data.entity.ExpenseEntity;
import ru.msvdev.homefinance.data.service.CategoryService;
import ru.msvdev.homefinance.data.service.ExpenseService;
import ru.msvdev.homefinance.io.csv.CsvIO;
import ru.msvdev.homefinance.io.csv.OperationCsvRow;
import ru.msvdev.homefinance.task.base.BaseTaskBuilder;
import ru.msvdev.homefinance.task.base.task.DataTaskBase;
import ru.msvdev.homefinance.task.operation.TaskCancel;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;


public class ImportFromCsvTaskBuilder extends BaseTaskBuilder<Void> {

    private final CategoryService categoryService;
    private final ExpenseService expenseService;

    private final CsvIO csvIO;
    private Path filePath;

    public ImportFromCsvTaskBuilder(ApplicationContext ctx) {
        super(ctx);
        categoryService = ctx.getBean(CategoryService.class);
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
     * Класс задачи импорта данных из CSV файла
     */
    private class ImportFromCsvTask extends DataTaskBase<Void> {

        private final Map<String, CategoryEntity> categoryEntityMap = new HashMap<>();
        private final Map<LocalDate, Map<String, Set<BigDecimal>>> expensesMap = new HashMap<>();

        @Override
        protected Void call() throws Exception {
            categoryService
                    .findAll()
                    .forEach(entity -> {
                        entity.setExpenses(null);
                        categoryEntityMap.put(entity.getName(), entity);
                    });

            expenseService
                    .findAll()
                    .forEach(entity -> {
                        LocalDate date = entity.getDate();
                        String category = entity.getCategory().getName();
                        BigDecimal cost = entity.getCost();

                        if (!expensesMap.containsKey(date)) {
                            expensesMap.put(date, new HashMap<>());
                        }
                        if (!expensesMap.get(date).containsKey(category)) {
                            expensesMap.get(date).put(category, new HashSet<>());
                        }
                        expensesMap.get(date).get(category).add(cost);
                    });

            List<OperationCsvRow> rowsFromCsv = csvIO.importFromCsv(filePath);

            for (OperationCsvRow row : rowsFromCsv) {
                CategoryEntity categoryEntity = saveCategory(row.getCategory());
                saveExpense(row.getDate(), categoryEntity, row.getCost(), row.getNote(), row.getCheck());
            }

            return null;
        }

        private CategoryEntity saveCategory(String categoryName) {
            if (categoryEntityMap.containsKey(categoryName)) {
                return categoryEntityMap.get(categoryName);
            }
            CategoryEntity entity = new CategoryEntity();
            entity.setName(categoryName);
            entity = categoryService.newCategory(entity);
            categoryEntityMap.put(entity.getName(), entity);
            return entity;
        }

        private void saveExpense(LocalDate date, CategoryEntity category, BigDecimal cost, String note, Boolean check) {
            if (expensesMap.containsKey(date) &&
                    expensesMap.get(date).containsKey(category.getName()) &&
                    expensesMap.get(date).get(category.getName()).contains(cost))
                return;

            ExpenseEntity entity = new ExpenseEntity();
            entity.setDate(date);
            entity.setCategory(category);
            entity.setCost(cost);
            entity.setNote(note);
            entity.setState(check ? ExpenseEntity.State.APPROVED : ExpenseEntity.State.NOT_APPROVED);

            entity = expenseService.newExpense(entity);

            String categoryName = entity.getCategory().getName();

            if (!expensesMap.containsKey(date)) {
                expensesMap.put(date, new HashMap<>());
            }
            if (!expensesMap.get(date).containsKey(categoryName)) {
                expensesMap.get(date).put(categoryName, new HashSet<>());
            }
            expensesMap.get(date).get(categoryName).add(cost);
        }
    }
}