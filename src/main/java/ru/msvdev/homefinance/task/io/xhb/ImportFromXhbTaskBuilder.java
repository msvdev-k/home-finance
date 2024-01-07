package ru.msvdev.homefinance.task.io.xhb;

import org.springframework.context.ApplicationContext;
import ru.msvdev.desktop.utils.task.BaseTaskBuilder;
import ru.msvdev.desktop.utils.task.DataTaskBase;
import ru.msvdev.desktop.utils.task.TaskCancel;
import ru.msvdev.homefinance.data.entity.CategoryEntity;
import ru.msvdev.homefinance.data.entity.ExpenseEntity;
import ru.msvdev.homefinance.data.service.CategoryService;
import ru.msvdev.homefinance.data.service.ExpenseService;
import ru.msvdev.homefinance.io.xhb.Category;
import ru.msvdev.homefinance.io.xhb.Operation;
import ru.msvdev.homefinance.io.xhb.XmlHBParser;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


public class ImportFromXhbTaskBuilder extends BaseTaskBuilder<Void> {

    private final CategoryService categoryService;
    private final ExpenseService expenseService;

    private final XmlHBParser xmlHBParser;
    private Path filePath;

    public ImportFromXhbTaskBuilder(ApplicationContext ctx) {
        super(ctx);
        categoryService = ctx.getBean(CategoryService.class);
        expenseService = ctx.getBean(ExpenseService.class);
        xmlHBParser = new XmlHBParser();
    }

    /**
     * Установить значение пути к файлу *.xhb
     *
     * @param filePath путь к файлу программы Home Bank (*.xhb)
     */
    public void setFilePath(Path filePath) {
        this.filePath = filePath;
    }

    public TaskCancel buildAndRun() {
        return buildAndRun(new ImportFromXhbTask());
    }


    /**
     * Класс задачи импорта данных из XHB файла
     */
    private class ImportFromXhbTask extends DataTaskBase<Void> {

        private final Map<String, CategoryEntity> categoryEntityMap = new HashMap<>();
        private final Set<Set<Object>> uniqueExpenses = new HashSet<>();

        @Override
        protected Void call() throws Exception {
            xmlHBParser.parseFile(filePath);

            Map<Integer, Category> categoryMap = xmlHBParser.getCategoryMap();
            List<Operation> operations = xmlHBParser.getOperations();

            categoryService
                    .findAll()
                    .forEach(entity -> {
                        entity.setExpenses(null);
                        categoryEntityMap.put(entity.getName(), entity);
                    });

            Set<CategoryEntity> newCategories = categoryMap
                    .values()
                    .stream()
                    .map(Category::toString)
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

            for (Operation operation : operations) {
                LocalDate date = operation.getDate();
                String categoryName = categoryMap.get(operation.getCategory()).toString();
                BigDecimal cost = operation.getAmount().setScale(2, RoundingMode.HALF_UP);
                String note = operation.getNote();

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
                expenseEntity.setState(ExpenseEntity.State.NOT_APPROVED);

                newExpenseEntities.add(expenseEntity);
                uniqueExpenses.add(expense);
            }

            expenseService.newExpense(newExpenseEntities);

            return null;
        }
    }
}
