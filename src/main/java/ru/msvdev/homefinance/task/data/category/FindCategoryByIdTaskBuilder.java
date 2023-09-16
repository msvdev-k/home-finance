package ru.msvdev.homefinance.task.data.category;

import lombok.Setter;
import org.springframework.context.ApplicationContext;
import ru.msvdev.homefinance.data.entity.CategoryEntity;
import ru.msvdev.homefinance.task.base.task.DataTaskBase;
import ru.msvdev.homefinance.task.operation.TaskCancel;
import ru.msvdev.homefinance.task.base.TaskException;


public class FindCategoryByIdTaskBuilder extends BaseCategoryTaskBuilder<CategoryEntity> {

    @Setter
    private Integer id;

    public FindCategoryByIdTaskBuilder(ApplicationContext ctx) {
        super(ctx);
    }

    public TaskCancel buildAndRun() {
        return buildAndRun(new FindCategoryByIdTask());
    }


    private class FindCategoryByIdTask extends DataTaskBase<CategoryEntity> {

        @Override
        protected CategoryEntity call() throws Exception {
            return categoryService
                    .findById(id)
                    .orElseThrow(() -> new TaskException("Категория с id = " + id + " не найдена", null));
        }
    }
}
