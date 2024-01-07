package ru.msvdev.homefinance.task.io.csv;

import org.springframework.context.ApplicationContext;
import ru.msvdev.desktop.utils.task.BaseTaskBuilder;
import ru.msvdev.desktop.utils.task.DataTaskBase;
import ru.msvdev.desktop.utils.task.TaskCancel;
import ru.msvdev.homefinance.data.entity.CategoryEntity;
import ru.msvdev.homefinance.data.entity.ExpenseEntity;
import ru.msvdev.homefinance.data.service.CategoryService;
import ru.msvdev.homefinance.data.service.ExpenseService;
import ru.msvdev.homefinance.io.csv.CsvIO;
import ru.msvdev.homefinance.io.csv.OperationCsvRow;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


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
        private final Set<Set<Object>> uniqueExpenses = new HashSet<>();

        @Override
        protected Void call() throws Exception {
            List<OperationCsvRow> rowsFromCsv = csvIO.importFromCsv(filePath);

            categoryService
                    .findAll()
                    .forEach(entity -> {
                        entity.setExpenses(null);
                        categoryEntityMap.put(entity.getName(), entity);
                    });

            Set<CategoryEntity> newCategories = rowsFromCsv.stream()
                    .map(OperationCsvRow::getCategory)
                    .filter(categoryName -> !categoryEntityMap.containsKey(categoryName))
                    .map(categoryName -> {
                        CategoryEntity categoryEntity = new CategoryEntity();
                        categoryEntity.setName(categoryName);
                        return categoryEntity;
                    })
                    .collect(Collectors.toSet());

            categoryService.newCategory(newCategories)
                    .forEach(entity -> {
                        entity.setExpenses(null);
                        categoryEntityMap.put(entity.getName(), entity);
                    });

            expenseService
                    .findAll()
                    .forEach(entity -> {
                        Set<Object> expense = new HashSet<>();
                        expense.add(entity.getDate());
                        expense.add(entity.getCategory().getName());
                        expense.add(entity.getCost().setScale(2, RoundingMode.HALF_UP));
                        uniqueExpenses.add(expense);
                    });

            List<ExpenseEntity> newExpenseEntities = new ArrayList<>();

            for (OperationCsvRow row : rowsFromCsv) {
                LocalDate date = row.getDate();
                String categoryName = row.getCategory();
                BigDecimal cost = row.getCost().setScale(2, RoundingMode.HALF_UP);
                String note = row.getNote();
                Boolean check = row.getCheck();

                CategoryEntity category = categoryEntityMap.get(categoryName);

                Set<Object> expense = new HashSet<>();
                expense.add(date);
                expense.add(category.getName());
                expense.add(cost);

                if (uniqueExpenses.contains(expense)) continue;

                ExpenseEntity expenseEntity = new ExpenseEntity();
                expenseEntity.setDate(date);
                expenseEntity.setCategory(category);
                expenseEntity.setCost(cost);
                expenseEntity.setNote(note);
                expenseEntity.setState(check ? ExpenseEntity.State.APPROVED : ExpenseEntity.State.NOT_APPROVED);

                newExpenseEntities.add(expenseEntity);
                uniqueExpenses.add(expense);
            }

            expenseService.newExpense(newExpenseEntities);

            return null;
        }
    }
}
