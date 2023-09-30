package ru.msvdev.homefinance.viewutils.table;

import javafx.scene.control.TableView;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class TableController<V, K> {

    protected final Map<K, V> rows;
    protected V newRow;

    protected TableView<V> tableView;

    {
        rows = new ConcurrentHashMap<>();
    }

    public void setTableView(TableView<V> tableView) {
        this.tableView = tableView;
    }


    abstract public void initTable();

    abstract public void refresh();

    abstract public void newRow();

    abstract public void removeSelected();
}
