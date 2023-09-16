package ru.msvdev.homefinance.task.data.category;

import org.springframework.context.ApplicationContext;
import ru.msvdev.homefinance.data.service.CategoryService;
import ru.msvdev.homefinance.task.base.BaseTaskBuilder;


public abstract class BaseCategoryTaskBuilder<T> extends BaseTaskBuilder<T> {

    protected final CategoryService categoryService;

    public BaseCategoryTaskBuilder(ApplicationContext ctx) {
        super(ctx);
        categoryService = ctx.getBean(CategoryService.class);
    }
}
