package ru.msvdev.homefinance.task.data.expense;

import lombok.Setter;
import org.springframework.context.ApplicationContext;
import ru.msvdev.homefinance.task.base.task.DataTaskBase;
import ru.msvdev.homefinance.task.operation.TaskCancel;


public class DeleteExpenseByIdTaskBuilder extends BaseExpenseTaskBuilder<Void> {

    @Setter
    private Integer id;


    public DeleteExpenseByIdTaskBuilder(ApplicationContext ctx) {
        super(ctx);
    }


    public TaskCancel buildAndRun() {
        if (id == null) {
            throw new NullPointerException("id - обязательный параметр не равный null");
        }

        return buildAndRun(new DeleteExpenseByIdPersonTask());
    }


    private class DeleteExpenseByIdPersonTask extends DataTaskBase<Void> {

        @Override
        protected Void call() throws Exception {
            expenseService.deleteById(id);
            return null;
        }
    }
}
