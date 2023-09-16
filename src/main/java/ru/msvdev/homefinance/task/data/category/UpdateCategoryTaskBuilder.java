package ru.msvdev.homefinance.task.data.category;

import lombok.Setter;
import org.springframework.context.ApplicationContext;
import ru.msvdev.homefinance.data.entity.CategoryEntity;
import ru.msvdev.homefinance.task.base.task.DataTaskBase;
import ru.msvdev.homefinance.task.operation.TaskCancel;


public class UpdateCategoryTaskBuilder extends BaseCategoryTaskBuilder<CategoryEntity> {

    @Setter
    private Integer id;
    @Setter
    private String name;
    @Setter
    private String description;


    public UpdateCategoryTaskBuilder(ApplicationContext ctx) {
        super(ctx);
    }


    public TaskCancel buildAndRun() {
        if (id == null) {
            throw new NullPointerException("id - обязательный параметр не равный null");
        }
        if (name == null) {
            throw new NullPointerException("name - обязательный параметр не равный null");
        }

        return buildAndRun(new UpdateCategoryTask());
    }


    private class UpdateCategoryTask extends DataTaskBase<CategoryEntity> {

        @Override
        protected CategoryEntity call() throws Exception {
            CategoryEntity entity = new CategoryEntity();
            entity.setId(id);
            entity.setName(name);
            entity.setDescription(description);

            return categoryService.updateCategory(entity);
        }
    }
}
