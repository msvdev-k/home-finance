package ru.msvdev.homefinance.task.data.expense;

import lombok.Setter;
import org.springframework.context.ApplicationContext;
import ru.msvdev.desktop.utils.task.TaskException;
import ru.msvdev.desktop.utils.task.DataTaskBase;
import ru.msvdev.desktop.utils.task.TaskCancel;
import ru.msvdev.homefinance.data.entity.ExpenseEntity;


@Setter
public class FindExpenseByIdTaskBuilder extends BaseExpenseTaskBuilder<ExpenseEntity> {

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
