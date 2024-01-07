package ru.msvdev.homefinance.task.data.category;

import lombok.Setter;
import org.springframework.context.ApplicationContext;
import ru.msvdev.desktop.utils.task.DataTaskBase;
import ru.msvdev.desktop.utils.task.TaskCancel;


@Setter
public class DeleteCategoryByIdTaskBuilder extends BaseCategoryTaskBuilder<Void> {

    private Integer id;


    public DeleteCategoryByIdTaskBuilder(ApplicationContext ctx) {
        super(ctx);
    }


    public TaskCancel buildAndRun() {
        if (id == null) {
            throw new NullPointerException("id - обязательный параметр не равный null");
        }

        return buildAndRun(new DeleteCategoryByIdPersonTask());
    }


    private class DeleteCategoryByIdPersonTask extends DataTaskBase<Void> {

        @Override
        protected Void call() throws Exception {
            categoryService.deleteById(id);
            return null;
        }
    }
}
