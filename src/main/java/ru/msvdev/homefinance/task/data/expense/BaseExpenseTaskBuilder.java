package ru.msvdev.homefinance.task.data.expense;

import org.springframework.context.ApplicationContext;
import ru.msvdev.homefinance.data.service.ExpenseService;
import ru.msvdev.homefinance.task.base.BaseTaskBuilder;


public abstract class BaseExpenseTaskBuilder<T> extends BaseTaskBuilder<T> {

    protected final ExpenseService expenseService;

    public BaseExpenseTaskBuilder(ApplicationContext ctx) {
        super(ctx);
        expenseService = ctx.getBean(ExpenseService.class);
    }
}
