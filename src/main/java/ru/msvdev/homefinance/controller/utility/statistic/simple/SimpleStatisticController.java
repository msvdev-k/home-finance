package ru.msvdev.homefinance.controller.utility.statistic.simple;

import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import lombok.RequiredArgsConstructor;
import org.controlsfx.control.PropertySheet;
import org.springframework.stereotype.Controller;
import ru.msvdev.desktop.utils.task.TaskBuilder;
import ru.msvdev.homefinance.controller.utility.statistic.simple.property.FilterPropertyContainer;
import ru.msvdev.homefinance.controller.utility.statistic.simple.table.CostColumn;
import ru.msvdev.homefinance.controller.utility.statistic.simple.table.PeriodColumn;
import ru.msvdev.homefinance.task.data.expense.FindAllExpensesTaskBuilder;
import ru.msvdev.homefinance.task.statistic.category.CategoryStatisticProcessor;
import ru.msvdev.homefinance.task.statistic.category.CategoryStatisticProcessorTaskBuilder;
import ru.msvdev.homefinance.task.statistic.category.CategoryStatisticItem;
import ru.msvdev.homefinance.task.statistic.category.CategoryStatisticSelectTaskBuilder;

import java.net.URL;
import java.util.ResourceBundle;


@Controller
@RequiredArgsConstructor
public class SimpleStatisticController implements Initializable {

    private final TaskBuilder taskBuilder;


    private FilterPropertyContainer filterPropertyContainer;
    private CategoryStatisticProcessor categoryStatisticProcessor;

    public PropertySheet categoryPropertySheet;
    public TableView<CategoryStatisticItem> statisticTable;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateStatistics();

        filterPropertyContainer = new FilterPropertyContainer();
        filterPropertyContainer.setEditor(categoryPropertySheet);
        filterPropertyContainer.setChangePropertyListener(this::categoryPropertyChange);

        categoryPropertySheet.setModeSwitcherVisible(false);
        categoryPropertySheet.setSearchBoxVisible(false);

        statisticTable.getColumns().add(new PeriodColumn());
        statisticTable.getColumns().add(new CostColumn());
    }


    public void updateStatistics() {
        FindAllExpensesTaskBuilder builder = taskBuilder.getBuilder(FindAllExpensesTaskBuilder.class);
        builder.addSucceededListener(expenseEntities -> {

            CategoryStatisticProcessorTaskBuilder statisticTaskBuilder =
                    taskBuilder.getBuilder(CategoryStatisticProcessorTaskBuilder.class);

            statisticTaskBuilder.setExpenseEntities(expenseEntities);
            statisticTaskBuilder.addSucceededListener(statisticProcessor -> {

                categoryStatisticProcessor = statisticProcessor;
                filterPropertyContainer.setCategories(statisticProcessor.getCategories());
                filterPropertyContainer.setFromDate(statisticProcessor.getMinDate());
                filterPropertyContainer.setToDate(statisticProcessor.getMaxDate());
                categoryPropertyChange(filterPropertyContainer);
            });

            statisticTaskBuilder.buildAndRun();
        });
        builder.buildAndRun();
    }

    private void categoryPropertyChange(FilterPropertyContainer propertyContainer) {
        if (categoryStatisticProcessor == null) return;

        CategoryStatisticSelectTaskBuilder builder =
                taskBuilder.getBuilder(CategoryStatisticSelectTaskBuilder.class);

        builder.setCategoryStatisticProcessor(categoryStatisticProcessor);
        builder.setCategory(propertyContainer.getCategory());
        builder.setFromDate(propertyContainer.getFromDate());
        builder.setToDate(propertyContainer.getToDate());
        builder.setPeriod(propertyContainer.getPeriod());

        builder.addSucceededListener(statisticTable.getItems()::setAll);
        builder.buildAndRun();
    }
}
