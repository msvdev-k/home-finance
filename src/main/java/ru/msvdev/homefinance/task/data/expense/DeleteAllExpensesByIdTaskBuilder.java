package ru.msvdev.homefinance.task.data.expense;

import org.springframework.context.ApplicationContext;
import ru.msvdev.desktop.utils.task.DataTaskBase;
import ru.msvdev.desktop.utils.task.TaskCancel;

import java.util.ArrayList;
import java.util.List;


public class DeleteAllExpensesByIdTaskBuilder extends BaseExpenseTaskBuilder<Void> {

    private final List<Integer> ids = new ArrayList<>();

    public void addId(Integer id) {
        ids.add(id);
    }

    public DeleteAllExpensesByIdTaskBuilder(ApplicationContext ctx) {
        super(ctx);
    }


    public TaskCancel buildAndRun() {
        return buildAndRun(new DeleteAllExpensesByIdPersonTask());
    }


    private class DeleteAllExpensesByIdPersonTask extends DataTaskBase<Void> {

        @Override
        protected Void call() throws Exception {
            expenseService.deleteById(ids);
            return null;
        }
    }
}
