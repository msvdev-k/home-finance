package ru.msvdev.homefinance.task.data.category;

import lombok.Setter;
import org.springframework.context.ApplicationContext;
import ru.msvdev.homefinance.data.entity.CategoryEntity;
import ru.msvdev.homefinance.task.base.task.DataTaskBase;
import ru.msvdev.homefinance.task.operation.TaskCancel;


public class NewCategoryTaskBuilder extends BaseCategoryTaskBuilder<CategoryEntity> {

    @Setter
    private String name;
    @Setter
    private String description;


    public NewCategoryTaskBuilder(ApplicationContext ctx) {
        super(ctx);
    }

    public TaskCancel buildAndRun() {
        if (name == null) {
            throw new NullPointerException("name - обязательный параметр не равный null");
        }

        return buildAndRun(new NewCategoryTask());
    }


    private class NewCategoryTask extends DataTaskBase<CategoryEntity> {

        @Override
        protected CategoryEntity call() throws Exception {
            CategoryEntity entity = new CategoryEntity();
            entity.setName(name);
            entity.setDescription(description);

            return categoryService.newCategory(entity);
        }
    }
}
