package ru.msvdev.homefinance.task.data.expense;

import lombok.Setter;
import org.springframework.context.ApplicationContext;
import ru.msvdev.homefinance.data.entity.CategoryEntity;
import ru.msvdev.homefinance.data.entity.ExpenseEntity;
import ru.msvdev.homefinance.task.base.task.DataTaskBase;
import ru.msvdev.homefinance.task.operation.TaskCancel;

import java.math.BigDecimal;
import java.time.LocalDate;


public class NewExpenseTaskBuilder extends BaseExpenseTaskBuilder<ExpenseEntity> {

    @Setter
    private LocalDate date;
    @Setter
    private CategoryEntity category;
    @Setter
    private BigDecimal cost;
    @Setter
    private ExpenseEntity.State state;
    @Setter
    private String note;


    public NewExpenseTaskBuilder(ApplicationContext ctx) {
        super(ctx);
    }

    public TaskCancel buildAndRun() {
        if (date == null) {
            throw new NullPointerException("date - обязательный параметр не равный null");
        }
        if (category == null) {
            throw new NullPointerException("category - обязательный параметр не равный null");
        }
        if (cost == null) {
            throw new NullPointerException("cost - обязательный параметр не равный null");
        }

        return buildAndRun(new NewExpenseTask());
    }


    private class NewExpenseTask extends DataTaskBase<ExpenseEntity> {

        @Override
        protected ExpenseEntity call() throws Exception {
            ExpenseEntity entity = new ExpenseEntity();
            entity.setDate(date);
            entity.setCategory(category);
            entity.setCost(cost);
            entity.setState(state);
            entity.setNote(note);

            return expenseService.newExpense(entity);
        }
    }
}
