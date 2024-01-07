package ru.msvdev.homefinance.task.data.category;

import lombok.Setter;
import org.springframework.context.ApplicationContext;
import ru.msvdev.desktop.utils.task.DataTaskBase;
import ru.msvdev.desktop.utils.task.TaskCancel;
import ru.msvdev.homefinance.data.entity.CategoryEntity;


@Setter
public class NewCategoryTaskBuilder extends BaseCategoryTaskBuilder<CategoryEntity> {

    private String name;
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
