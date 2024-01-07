package ru.msvdev.homefinance.task.data.expense;

import org.springframework.context.ApplicationContext;
import ru.msvdev.desktop.utils.task.DataTaskBase;
import ru.msvdev.desktop.utils.task.TaskCancel;
import ru.msvdev.homefinance.data.entity.ExpenseEntity;

import java.util.List;


public class FindAllExpensesTaskBuilder extends BaseExpenseTaskBuilder<List<ExpenseEntity>> {

    public FindAllExpensesTaskBuilder(ApplicationContext ctx) {
        super(ctx);
    }

    public TaskCancel buildAndRun() {
        return buildAndRun(new FindAllExpensesTask());
    }


    private class FindAllExpensesTask extends DataTaskBase<List<ExpenseEntity>> {

        @Override
        protected List<ExpenseEntity> call() throws Exception {
            return expenseService.findAll();
        }
    }
}
