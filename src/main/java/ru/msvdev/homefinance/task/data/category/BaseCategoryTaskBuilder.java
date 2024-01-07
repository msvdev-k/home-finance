package ru.msvdev.homefinance.task.data.category;

import org.springframework.context.ApplicationContext;
import ru.msvdev.desktop.utils.task.BaseTaskBuilder;
import ru.msvdev.homefinance.data.service.CategoryService;


public abstract class BaseCategoryTaskBuilder<T> extends BaseTaskBuilder<T> {

    protected final CategoryService categoryService;

    public BaseCategoryTaskBuilder(ApplicationContext ctx) {
        super(ctx);
        categoryService = ctx.getBean(CategoryService.class);
    }
}
