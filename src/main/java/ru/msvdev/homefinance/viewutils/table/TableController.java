package ru.msvdev.homefinance.viewutils.table;

import javafx.scene.control.TableView;
import ru.msvdev.homefinance.task.operation.TaskBuilder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class TableController<V, K> {

    protected final TaskBuilder taskBuilder;

    protected final Map<K, V> rows;
    protected V newRow;

    protected TableView<V> tableView;

    {
        rows = new ConcurrentHashMap<>();
    }

    public TableController(TaskBuilder taskBuilder) {
        this.taskBuilder = taskBuilder;
    }

    public void setTableView(TableView<V> tableView) {
        this.tableView = tableView;
    }


    public void initTable() {
        rows.clear();
        newRow = null;

        tableView.getColumns().clear();
    }

    public void refresh() {
        rows.clear();
        newRow = null;

        tableView.getItems().clear();
    }

    abstract public void newRow();

    abstract public void removeSelected();
}
