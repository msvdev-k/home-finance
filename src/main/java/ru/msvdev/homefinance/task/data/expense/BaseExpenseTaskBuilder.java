package ru.msvdev.homefinance.task.data.expense;

import org.springframework.context.ApplicationContext;
import ru.msvdev.desktop.utils.task.BaseTaskBuilder;
import ru.msvdev.homefinance.data.service.ExpenseService;


public abstract class BaseExpenseTaskBuilder<T> extends BaseTaskBuilder<T> {

    protected final ExpenseService expenseService;

    public BaseExpenseTaskBuilder(ApplicationContext ctx) {
        super(ctx);
        expenseService = ctx.getBean(ExpenseService.class);
    }
}
