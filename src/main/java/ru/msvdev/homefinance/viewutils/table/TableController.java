package ru.msvdev.homefinance.viewutils.table;

import javafx.scene.control.TableView;
import ru.msvdev.homefinance.task.operation.TaskBuilder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class TableController<ROW extends RowModel<?, ID>, ID> {

    protected final TaskBuilder taskBuilder;

    protected final Map<ID, ROW> rows;
    protected ROW newRow;

    protected TableView<ROW> tableView;

    {
        rows = new ConcurrentHashMap<>();
    }

    public TableController(TaskBuilder taskBuilder) {
        this.taskBuilder = taskBuilder;
    }

    public void setTableView(TableView<ROW> tableView) {
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

    protected void saveRowEventListener(ROW rowModel) {
        if (rowModel == newRow) {
            newRow = null;
            rows.put(rowModel.idProperty().get(), rowModel);
        }
    }

    abstract public void addNewRow();

    abstract public void removeSelected();
}
