package ru.msvdev.homefinance.task.data.expense;

import lombok.Setter;
import org.springframework.context.ApplicationContext;
import ru.msvdev.desktop.utils.task.DataTaskBase;
import ru.msvdev.desktop.utils.task.TaskCancel;
import ru.msvdev.homefinance.data.entity.CategoryEntity;
import ru.msvdev.homefinance.data.entity.ExpenseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;


@Setter
public class UpdateExpenseTaskBuilder extends BaseExpenseTaskBuilder<ExpenseEntity> {

    private Integer id;
    private LocalDate date;
    private CategoryEntity category;
    private BigDecimal cost;
    private ExpenseEntity.State state;
    private String note;


    public UpdateExpenseTaskBuilder(ApplicationContext ctx) {
        super(ctx);
    }


    public TaskCancel buildAndRun() {
        if (id == null) {
            throw new NullPointerException("id - обязательный параметр не равный null");
        }
        if (date == null) {
            throw new NullPointerException("date - обязательный параметр не равный null");
        }
        if (category == null) {
            throw new NullPointerException("category - обязательный параметр не равный null");
        }
        if (cost == null) {
            throw new NullPointerException("cost - обязательный параметр не равный null");
        }

        return buildAndRun(new UpdateExpenseTask());
    }


    private class UpdateExpenseTask extends DataTaskBase<ExpenseEntity> {

        @Override
        protected ExpenseEntity call() throws Exception {
            ExpenseEntity entity = new ExpenseEntity();
            entity.setId(id);
            entity.setDate(date);
            entity.setCategory(category);
            entity.setCost(cost);
            entity.setState(state);
            entity.setNote(note);

            return expenseService.updateExpense(entity);
        }
    }
}
