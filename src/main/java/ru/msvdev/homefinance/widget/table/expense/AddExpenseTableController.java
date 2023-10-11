package ru.msvdev.homefinance.widget.table.expense;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import ru.msvdev.homefinance.data.entity.CategoryEntity;
import ru.msvdev.homefinance.task.data.category.FindAllCategoriesTaskBuilder;
import ru.msvdev.homefinance.task.data.expense.DeleteExpenseByIdTaskBuilder;
import ru.msvdev.homefinance.task.operation.TaskBuilder;
import ru.msvdev.homefinance.viewutils.table.TableController;
import ru.msvdev.homefinance.viewutils.table.cell.DateCellModel;
import ru.msvdev.homefinance.viewutils.table.cell.MoneyCellModel;
import ru.msvdev.homefinance.viewutils.table.cell.StringCellModel;
import ru.msvdev.homefinance.widget.table.expense.columnbuilder.CategoryColumnBuilder;
import ru.msvdev.homefinance.widget.table.expense.columnbuilder.CostColumnBuilder;
import ru.msvdev.homefinance.widget.table.expense.columnbuilder.DateColumnBuilder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;


public class AddExpenseTableController extends TableController<ExpenseRowModel, Integer> {

    private final Map<String, CategoryEntity> categoryEntityMap = new ConcurrentHashMap<>();

    private TableColumn<ExpenseRowModel, DateCellModel> dateColumn;
    private TableColumn<ExpenseRowModel, StringCellModel> categoryColumn;
    private TableColumn<ExpenseRowModel, MoneyCellModel> costColumn;


    public AddExpenseTableController(TaskBuilder taskBuilder) {
        super(taskBuilder);
    }


    @Override
    public void initTable() {
        super.initTable();
        refreshCategoryEntityMap();

        DateColumnBuilder dateColumnBuilder = new DateColumnBuilder();
        dateColumn = dateColumnBuilder.build();

        CategoryColumnBuilder categoryColumnBuilder = new CategoryColumnBuilder();
        categoryColumn = categoryColumnBuilder.build();

        CostColumnBuilder costColumnBuilder = new CostColumnBuilder();
        costColumn = costColumnBuilder.build();

        tableView.getColumns().add(dateColumn);
        tableView.getColumns().add(categoryColumn);
        tableView.getColumns().add(costColumn);
    }


    private ExpenseRowModel getNewRow() {
        ExpenseRowModel rowModel = ExpenseRowModel.getInstance();

        rowModel.setTaskBuilder(taskBuilder);
        rowModel.setCategoryEntityMap(categoryEntityMap);
        rowModel.setSaveRowEventListener(this::saveRowEventListener);

        return rowModel;
    }

    private void refreshCategoryEntityMap() {
        categoryEntityMap.clear();

        FindAllCategoriesTaskBuilder builder = taskBuilder.getBuilder(FindAllCategoriesTaskBuilder.class);
        builder.addSucceededListener(entities -> entities.forEach(e -> categoryEntityMap.put(e.getName(), e)));
        builder.buildAndRun();
    }


    @Override
    protected void saveRowEventListener(ExpenseRowModel rowModel) {
        super.saveRowEventListener(rowModel);
        if (newRow == null) {
            addNewRow();
            return;
        }
    }

    @Override
    public void refresh() {
        super.refresh();
        refreshCategoryEntityMap();
    }


    @Override
    public void addNewRow() {
        if (newRow != null) return;

        newRow = getNewRow();
        tableView.getItems().add(newRow);

        tableView.getSelectionModel().select(newRow);
        int index = tableView.getSelectionModel().getSelectedIndex();
        tableView.getSelectionModel().select(index, dateColumn);
        tableView.requestFocus();
    }

    @Override
    public void removeSelected() {
        ObservableList<ExpenseRowModel> selectedItems = tableView.getSelectionModel().getSelectedItems();
        DeleteExpenseByIdTaskBuilder builder = taskBuilder.getBuilder(DeleteExpenseByIdTaskBuilder.class);

        for (ExpenseRowModel item : selectedItems) {
            if (item == newRow) {
                newRow = null;
                tableView.getItems().remove(item);
                addNewRow();
                continue;
            }

            builder.setId(item.idProperty().get());
            builder.addSucceededListener(new DeleteRowListener(item));
            builder.buildAndRun();
        }
    }


    private class DeleteRowListener implements Consumer<Void> {
        private final ExpenseRowModel rowModel;

        public DeleteRowListener(ExpenseRowModel rowModel) {
            this.rowModel = rowModel;
        }

        @Override
        public void accept(Void unused) {
            tableView.getItems().remove(rowModel);
            rows.remove(rowModel.idProperty().get());
        }
    }
}
