package ru.msvdev.homefinance.task.statistic.category;

import lombok.Setter;
import org.springframework.context.ApplicationContext;
import ru.msvdev.desktop.utils.task.BaseTaskBuilder;
import ru.msvdev.desktop.utils.task.TaskBase;
import ru.msvdev.desktop.utils.task.TaskCancel;
import ru.msvdev.homefinance.controller.utility.statistic.simple.property.FilterPropertyContainer;

import java.time.LocalDate;
import java.util.List;


@Setter
public class CategoryStatisticSelectTaskBuilder extends BaseTaskBuilder<List<CategoryStatisticItem>> {

    private CategoryStatisticProcessor categoryStatisticProcessor;
    private String category;
    private LocalDate fromDate;
    private LocalDate toDate;
    private FilterPropertyContainer.Period period;


    public CategoryStatisticSelectTaskBuilder(ApplicationContext ctx) {
        super(ctx);
    }


    public TaskCancel buildAndRun() {
        return buildAndRun(new SelectTask());
    }

    /**
     * Класс задачи выборки данных
     */
    private class SelectTask extends TaskBase<List<CategoryStatisticItem>> {

        @Override
        protected List<CategoryStatisticItem> call() throws Exception {
            return categoryStatisticProcessor.select(category, fromDate, toDate, period);
        }
    }

}
