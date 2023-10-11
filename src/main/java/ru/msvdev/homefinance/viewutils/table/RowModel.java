package ru.msvdev.homefinance.viewutils.table;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import ru.msvdev.homefinance.task.operation.TaskBuilder;

import java.util.function.Consumer;


public abstract class RowModel<ROW extends RowModel<?, ID>, ID> {

    protected TaskBuilder taskBuilder;
    protected Consumer<ROW> saveRowEventListener;

    protected final ObjectProperty<ID> id = new SimpleObjectProperty<>();

    public ObjectProperty<ID> idProperty() {
        return id;
    }

    public void setTaskBuilder(TaskBuilder taskBuilder) {
        this.taskBuilder = taskBuilder;
    }

    public void setSaveRowEventListener(Consumer<ROW> saveRowEventListener) {
        this.saveRowEventListener = saveRowEventListener;
    }
}
