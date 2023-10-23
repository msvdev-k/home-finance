package ru.msvdev.homefinance.widget.table.expense;

import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import ru.msvdev.homefinance.data.entity.CategoryEntity;
import ru.msvdev.homefinance.data.entity.ExpenseEntity;
import ru.msvdev.homefinance.task.data.category.FindAllCategoriesTaskBuilder;
import ru.msvdev.homefinance.task.data.expense.DeleteExpenseByIdTaskBuilder;
import ru.msvdev.homefinance.task.data.expense.FindAllExpensesTaskBuilder;
import ru.msvdev.homefinance.task.operation.TaskBuilder;
import ru.msvdev.homefinance.viewutils.table.TableController;
import ru.msvdev.homefinance.viewutils.table.cell.BooleanCellModel;
import ru.msvdev.homefinance.viewutils.table.cell.DateCellModel;
import ru.msvdev.homefinance.viewutils.table.cell.MoneyCellModel;
import ru.msvdev.homefinance.viewutils.table.cell.StringCellModel;
import ru.msvdev.homefinance.widget.table.expense.columnbuilder.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;


public class ExpenseTableController extends TableController<ExpenseRowModel, Integer> {

    private final Map<String, CategoryEntity> categoryEntityMap = new ConcurrentHashMap<>();
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
        DeleteExpenseByIdTaskBuilder builder = taskBuilder.getBuilder(DeleteExpenseByIdTaskBuilder.class);

        for (ExpenseRowModel item : selectedItems) {
            if (item == newRow) {
                newRow = null;
                savedNewRow = null;
                tableView.getItems().remove(item);
                continue;
            }

            builder.setId(item.idProperty().get());
            builder.addSucceededListener(new DeleteRowListener(item));
            builder.buildAndRun();
        }
    }

    public void setStatisticListener(BiConsumer<Integer, BigDecimal> statisticListener) {
        this.statisticListener = statisticListener;
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

    private class DeleteRowListener implements Consumer<Void> {
        private final ExpenseRowModel rowModel;

        public DeleteRowListener(ExpenseRowModel rowModel) {
            this.rowModel = rowModel;
        }

        @Override
        public void accept(Void unused) {
            tableView.getItems().remove(rowModel);
            rows.remove(rowModel.idProperty().get());
            updateStatistic();
        }
    }
}
