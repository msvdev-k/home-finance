package ru.msvdev.homefinance.task.operation;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@Component
@RequiredArgsConstructor
public class TaskBuilder {

    private final ApplicationContext ctx;

    public <T> T getBuilder(Class<T> builderClass) {
        try {
            Constructor<T> constructor = builderClass.getConstructor(ApplicationContext.class);
            return constructor.newInstance(ctx);

        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
