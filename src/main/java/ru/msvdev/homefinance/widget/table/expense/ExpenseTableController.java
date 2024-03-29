package ru.msvdev.homefinance.widget.table.expense;

import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import lombok.Setter;
import ru.msvdev.desktop.utils.task.TaskBuilder;
import ru.msvdev.desktop.utils.widget.datatable.AbstractTableController;
import ru.msvdev.desktop.utils.widget.datatable.cell.model.BooleanCellModel;
import ru.msvdev.desktop.utils.widget.datatable.cell.model.DateCellModel;
import ru.msvdev.desktop.utils.widget.datatable.cell.model.MoneyCellModel;
import ru.msvdev.desktop.utils.widget.datatable.cell.model.StringCellModel;
import ru.msvdev.homefinance.data.entity.CategoryEntity;
import ru.msvdev.homefinance.data.entity.ExpenseEntity;
import ru.msvdev.homefinance.task.data.category.FindAllCategoriesTaskBuilder;
import ru.msvdev.homefinance.task.data.expense.DeleteAllExpensesByIdTaskBuilder;
import ru.msvdev.homefinance.task.data.expense.FindAllExpensesTaskBuilder;
import ru.msvdev.homefinance.widget.table.expense.columnbuilder.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;


public class ExpenseTableController extends AbstractTableController<ExpenseRowModel, Integer> {

    private final Map<String, CategoryEntity> categoryEntityMap = new ConcurrentHashMap<>();
    @Setter
    private BiConsumer<Integer, BigDecimal> statisticListener;


    public ExpenseTableController(TaskBuilder taskBuilder) {
        super(taskBuilder);
    }


    @Override
    public void initTable() {
        super.initTable();

        DateColumnBuilder dateColumnBuilder = new DateColumnBuilder();
        TableColumn<ExpenseRowModel, DateCellModel> dateColumn = dateColumnBuilder.build();

        CategoryColumnBuilder categoryColumnBuilder = new CategoryColumnBuilder();
        TableColumn<ExpenseRowModel, StringCellModel> categoryColumn = categoryColumnBuilder.build();

        CostColumnBuilder costColumnBuilder = new CostColumnBuilder();
        TableColumn<ExpenseRowModel, MoneyCellModel> costColumn = costColumnBuilder.build();

        NoteColumnBuilder noteColumnBuilder = new NoteColumnBuilder();
        TableColumn<ExpenseRowModel, StringCellModel> noteColumn = noteColumnBuilder.build();

        CheckColumnBuilder checkColumnBuilder = new CheckColumnBuilder();
        TableColumn<ExpenseRowModel, BooleanCellModel> checkColumn = checkColumnBuilder.build();

        tableView.getColumns().add(dateColumn);
        tableView.getColumns().add(categoryColumn);
        tableView.getColumns().add(costColumn);
        tableView.getColumns().add(noteColumn);
        tableView.getColumns().add(checkColumn);

        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.getSelectionModel().selectedIndexProperty().addListener(observable -> updateStatistic());

        refresh();
    }


    @Override
    protected void saveRowEventListener(ExpenseRowModel rowModel) {
        super.saveRowEventListener(rowModel);
        updateStatistic();
    }

    @Override
    public void refresh() {
        super.refresh();
        refreshExpensesEntity();
        refreshCategoryEntityMap();
    }

    private ExpenseRowModel getNewRow() {
        ExpenseRowModel rowModel = ExpenseRowModel.getInstance();

        rowModel.setTaskBuilder(taskBuilder);
        rowModel.setCategoryEntityMap(categoryEntityMap);
        rowModel.setSaveRowEventListener(this::saveRowEventListener);

        return rowModel;
    }

    private void refreshExpensesEntity() {
        ObservableList<ExpenseRowModel> items = tableView.getItems();

        FindAllExpensesTaskBuilder builder = taskBuilder.getBuilder(FindAllExpensesTaskBuilder.class);
        builder.addSucceededListener(entities -> {
            for (ExpenseEntity entity : entities) {
                ExpenseRowModel rowModel = getNewRow();

                rowModel.idProperty().set(entity.getId());

                rowModel.dateProperty().get().setValue(entity.getDate());
                rowModel.dateProperty().get().resetError();

                rowModel.categoryProperty().get().setValue(entity.getCategory().getName());
                rowModel.categoryProperty().get().resetError();

                rowModel.costProperty().get().setValue(entity.getCost());
                rowModel.costProperty().get().resetError();

                rowModel.checkProperty().get().setValue(
                        entity.getState() != null && entity.getState() == ExpenseEntity.State.APPROVED
                );

                if (entity.getNote() != null) {
                    rowModel.noteProperty().get().setValue(entity.getNote());
                    rowModel.noteProperty().get().resetError();
                }

                rows.put(entity.getId(), rowModel);

                items.add(rowModel);
            }
        });
        builder.buildAndRun();
    }

    private void refreshCategoryEntityMap() {
        categoryEntityMap.clear();

        FindAllCategoriesTaskBuilder builder = taskBuilder.getBuilder(FindAllCategoriesTaskBuilder.class);
        builder.addSucceededListener(entities -> entities.forEach(e -> categoryEntityMap.put(e.getName(), e)));
        builder.buildAndRun();
    }


    @Override
    public void addNewRow() {
    }

    @Override
    public void removeSelected() {
        ObservableList<ExpenseRowModel> selectedItems = tableView.getSelectionModel().getSelectedItems();
        DeleteAllExpensesByIdTaskBuilder builder = taskBuilder.getBuilder(DeleteAllExpensesByIdTaskBuilder.class);
        DeleteRowsListener deleteRowsListener = new DeleteRowsListener();

        for (ExpenseRowModel item : selectedItems) {
            if (item == newRow) {
                newRow = null;
                savedNewRow = null;
                tableView.getItems().remove(item);
                continue;
            }

            builder.addId(item.idProperty().get());
            deleteRowsListener.addRowModel(item);
        }

        builder.addSucceededListener(deleteRowsListener);
        builder.buildAndRun();
    }

    private void updateStatistic() {
        if (statisticListener != null) {
            ObservableList<ExpenseRowModel> selectedItems = tableView.getSelectionModel().getSelectedItems();
            BigDecimal summ = BigDecimal.ZERO;
            for (ExpenseRowModel row : selectedItems) {
                summ = summ.add(row.costProperty().get().getValue());
            }
            statisticListener.accept(selectedItems.size(), summ);
        }
    }

    private class DeleteRowsListener implements Consumer<Void> {
        private final List<ExpenseRowModel> rowModels = new ArrayList<>();

        public void addRowModel(ExpenseRowModel rowModel) {
            rowModels.add(rowModel);
        }

        @Override
        public void accept(Void unused) {
            rowModels.forEach(rowModel -> rows.remove(rowModel.idProperty().get()));
            tableView.getItems().removeAll(rowModels);
            updateStatistic();
        }
    }
}
