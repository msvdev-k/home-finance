package ru.msvdev.homefinance.task.statistic.category;

import lombok.Setter;
import org.springframework.context.ApplicationContext;
import ru.msvdev.desktop.utils.task.BaseTaskBuilder;
import ru.msvdev.desktop.utils.task.TaskBase;
import ru.msvdev.desktop.utils.task.TaskCancel;
import ru.msvdev.homefinance.data.entity.ExpenseEntity;

import java.util.List;


@Setter
public class CategoryStatisticProcessorTaskBuilder extends BaseTaskBuilder<CategoryStatisticProcessor> {

    private List<ExpenseEntity> expenseEntities;


    public CategoryStatisticProcessorTaskBuilder(ApplicationContext ctx) {
        super(ctx);
    }


    public TaskCancel buildAndRun() {
        return buildAndRun(new StatisticProcessorTask());
    }


    /**
     * Класс задачи формирования объекта описания статистики
     */
    private class StatisticProcessorTask extends TaskBase<CategoryStatisticProcessor> {

        @Override
        protected CategoryStatisticProcessor call() throws Exception {
            CategoryStatisticProcessor statisticEngine = new CategoryStatisticProcessor();
            statisticEngine.prepare(expenseEntities);

            return statisticEngine;
        }
    }


}
