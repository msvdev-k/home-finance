package ru.msvdev.homefinance.task.data.category;

import org.springframework.context.ApplicationContext;
import ru.msvdev.homefinance.data.entity.CategoryEntity;
import ru.msvdev.homefinance.task.base.task.DataTaskBase;
import ru.msvdev.homefinance.task.operation.TaskCancel;

import java.util.List;


public class FindAllCategoriesTaskBuilder extends BaseCategoryTaskBuilder<List<CategoryEntity>> {

    public FindAllCategoriesTaskBuilder(ApplicationContext ctx) {
        super(ctx);
    }

    public TaskCancel buildAndRun() {
        return buildAndRun(new FindAllCategoriesTask());
    }


    private class FindAllCategoriesTask extends DataTaskBase<List<CategoryEntity>> {

        @Override
        protected List<CategoryEntity> call() throws Exception {
            return categoryService.findAll();
        }
    }
}
