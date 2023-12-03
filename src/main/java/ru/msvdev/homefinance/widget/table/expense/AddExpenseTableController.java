package ru.msvdev.homefinance.widget.table.expense;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import ru.msvdev.homefinance.data.entity.CategoryEntity;
import ru.msvdev.homefinance.data.entity.ExpenseEntity;
import ru.msvdev.homefinance.task.data.category.FindAllCategoriesTaskBuilder;
import ru.msvdev.homefinance.task.data.expense.DeleteExpenseByIdTaskBuilder;
import ru.msvdev.homefinance.task.data.expense.FindAllExpensesTaskBuilder;
import ru.msvdev.homefinance.task.operation.TaskBuilder;
import ru.msvdev.homefinance.viewutils.table.TableController;
import ru.msvdev.homefinance.viewutils.table.cell.DateCellModel;
import ru.msvdev.homefinance.viewutils.table.cell.MoneyCellModel;
import ru.msvdev.homefinance.viewutils.table.cell.StringCellModel;
import ru.msvdev.homefinance.widget.table.expense.columnbuilder.CategoryColumnBuilder;
import ru.msvdev.homefinance.widget.table.expense.columnbuilder.CostColumnBuilder;
import ru.msvdev.homefinance.widget.table.expense.columnbuilder.DateColumnBuilder;
import ru.msvdev.homefinance.widget.table.expense.columnbuilder.NoteColumnBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;


public class AddExpenseTableController extends TableController<ExpenseRowModel, Integer> {

    private final Set<String> notes = ConcurrentHashMap.newKeySet();
    private final Map<String, CategoryEntity> categoryEntityMap = new ConcurrentHashMap<>();

    private TableColumn<ExpenseRowModel, DateCellModel> dateColumn;

    private BiConsumer<Integer, BigDecimal> statisticListener;

    public AddExpenseTableController(TaskBuilder taskBuilder) {
        super(taskBuilder);
    }


    @Override
    public void initTable() {
        super.initTable();
        refreshNotes();
        refreshCategoryEntityMap();

        tableView.getSelectionModel().setCellSelectionEnabled(true);
        tableView.setEditable(true);

        DateColumnBuilder dateColumnBuilder = new DateColumnBuilder();
        dateColumn = dateColumnBuilder.build();

        CategoryColumnBuilder categoryColumnBuilder = new CategoryColumnBuilder();
        TableColumn<ExpenseRowModel, StringCellModel> categoryColumn = categoryColumnBuilder.build();

        CostColumnBuilder costColumnBuilder = new CostColumnBuilder();
        TableColumn<ExpenseRowModel, MoneyCellModel> costColumn = costColumnBuilder.build();

        NoteColumnBuilder noteColumnBuilder = new NoteColumnBuilder();
        TableColumn<ExpenseRowModel, StringCellModel> noteColumn = noteColumnBuilder.build();

        tableView.getColumns().add(dateColumn);
        tableView.getColumns().add(categoryColumn);
        tableView.getColumns().add(costColumn);
        tableView.getColumns().add(noteColumn);

        addNewRow();
    }


    private ExpenseRowModel getNewRow() {
        ExpenseRowModel rowModel = ExpenseRowModel.getInstance();

        rowModel.setTaskBuilder(taskBuilder);
        rowModel.setNotes(notes);
        rowModel.setCategoryEntityMap(categoryEntityMap);
        rowModel.setSaveRowEventListener(this::saveRowEventListener);

        if (savedNewRow != null) {
            rowModel.setDate(savedNewRow.dateProperty().get().getValue());
            rowModel.setCategory(savedNewRow.categoryProperty().get().getValue());
        } else {
            rowModel.setDate(LocalDate.now());
        }

        return rowModel;
    }

    private void refreshNotes() {
        notes.clear();

        FindAllExpensesTaskBuilder builder = taskBuilder.getBuilder(FindAllExpensesTaskBuilder.class);
        builder.addSucceededListener(entities -> {
            for (ExpenseEntity entity : entities) {
                String note = entity.getNote();
                if (note != null) notes.add(note);
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
    protected void saveRowEventListener(ExpenseRowModel rowModel) {
        super.saveRowEventListener(rowModel);
        updateStatistic();

        if (newRow == null) addNewRow();

        String note = rowModel.noteProperty().get().getValue();
        if (note != null) notes.add(note);
    }

    @Override
    public void refresh() {
        super.refresh();
        refreshNotes();
        refreshCategoryEntityMap();
        updateStatistic();
        addNewRow();
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
                savedNewRow = null;
                tableView.getItems().remove(item);
                addNewRow();
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
            BigDecimal summ = BigDecimal.ZERO;
            for (ExpenseRowModel row : rows.values()) {
                summ = summ.add(row.costProperty().get().getValue());
            }
            statisticListener.accept(rows.size(), summ);
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
