package ru.msvdev.homefinance.task.data.expense;

import lombok.Setter;
import org.springframework.context.ApplicationContext;
import ru.msvdev.homefinance.data.entity.ExpenseEntity;
import ru.msvdev.homefinance.task.base.TaskException;
import ru.msvdev.homefinance.task.base.task.DataTaskBase;
import ru.msvdev.homefinance.task.operation.TaskCancel;


public class FindExpenseByIdTaskBuilder extends BaseExpenseTaskBuilder<ExpenseEntity> {

    @Setter
    private Integer id;


    public FindExpenseByIdTaskBuilder(ApplicationContext ctx) {
        super(ctx);
    }

    public TaskCancel buildAndRun() {
        return buildAndRun(new FindExpenseByIdTask());
    }


    private class FindExpenseByIdTask extends DataTaskBase<ExpenseEntity> {

        @Override
        protected ExpenseEntity call() throws Exception {
            return expenseService
                    .findById(id)
                    .orElseThrow(() -> new TaskException("Расход с id = " + id + " не найден", null));
        }
    }
}
