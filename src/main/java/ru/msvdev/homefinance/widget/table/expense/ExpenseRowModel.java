package ru.msvdev.homefinance.widget.table.expense;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import ru.msvdev.homefinance.data.entity.CategoryEntity;
import ru.msvdev.homefinance.data.entity.ExpenseEntity;
import ru.msvdev.homefinance.task.data.expense.NewExpenseTaskBuilder;
import ru.msvdev.homefinance.task.data.expense.UpdateExpenseTaskBuilder;
import ru.msvdev.homefinance.viewutils.table.cell.*;
import ru.msvdev.homefinance.viewutils.table.RowModel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;


public class ExpenseRowModel extends RowModel<ExpenseRowModel, Integer> {

    private Map<String, CategoryEntity> categoryEntityMap;

    private final ObjectProperty<DateCellModel> date;
    private final ObjectProperty<StringCellModel> category;
    private final ObjectProperty<MoneyCellModel> cost;
    private final ObjectProperty<BooleanCellModel> check;

    {
        date = new SimpleObjectProperty<>();
        category = new SimpleObjectProperty<>();
        cost = new SimpleObjectProperty<>();
        check = new SimpleObjectProperty<>();
    }


    private ExpenseRowModel() {
    }


    public static ExpenseRowModel getInstance() {
        ExpenseRowModel rowModel = new ExpenseRowModel();

        DateCellModel dateCellModel = new DateCellModel();
        dateCellModel.setError(CellModel.CellError.REQUIRED);
        rowModel.date.set(dateCellModel);

        StringCellModel stringCellModel = new StringCellModel();
        stringCellModel.setError(CellModel.CellError.REQUIRED);
        rowModel.category.set(stringCellModel);

        MoneyCellModel moneyCellModel = new MoneyCellModel();
        moneyCellModel.setError(CellModel.CellError.REQUIRED);
        moneyCellModel.setSign(MoneyCellModel.RUBLE_SIGN);
        rowModel.cost.set(moneyCellModel);

        BooleanCellModel booleanCellModel = new BooleanCellModel();
        rowModel.check.set(booleanCellModel);

        return rowModel;
    }


    public void setCategoryEntityMap(Map<String, CategoryEntity> categoryEntityMap) {
        this.categoryEntityMap = categoryEntityMap;
        category.get().setValidValues(categoryEntityMap.keySet());
    }


    private void save() {
        if (date.get().isError() || category.get().isError() || cost.get().isError()) return;

        if (id.get() == null) {
            NewExpenseTaskBuilder builder = taskBuilder.getBuilder(NewExpenseTaskBuilder.class);

            builder.setDate(date.get().getValue());
            builder.setCategory(categoryEntityMap.get(category.get().getValue()));
            builder.setCost(cost.get().getValue());
            builder.setState(check.get().getValue() ? ExpenseEntity.State.APPROVED : ExpenseEntity.State.NOT_APPROVED);
            builder.addSucceededListener(this::succeededListener);
            builder.buildAndRun();

        } else {
            UpdateExpenseTaskBuilder builder = taskBuilder.getBuilder(UpdateExpenseTaskBuilder.class);

            builder.setDate(date.get().getValue());
            builder.setCategory(categoryEntityMap.get(category.get().getValue()));
            builder.setCost(cost.get().getValue());
            builder.setState(check.get().getValue() ? ExpenseEntity.State.APPROVED : ExpenseEntity.State.NOT_APPROVED);
            builder.addSucceededListener(this::succeededListener);
            builder.buildAndRun();
        }
    }

    private void succeededListener(ExpenseEntity entity) {
        id.set(entity.getId());

        DateCellModel dateCellModel = new DateCellModel();
        dateCellModel.setValue(entity.getDate());
        date.set(dateCellModel);

        StringCellModel stringCellModel = new StringCellModel();
        stringCellModel.setValue(entity.getCategory().getName());
        category.set(stringCellModel);

        MoneyCellModel moneyCellModel = new MoneyCellModel();
        moneyCellModel.setSign(MoneyCellModel.RUBLE_SIGN);
        moneyCellModel.setValue(entity.getCost());
        cost.set(moneyCellModel);

        BooleanCellModel booleanCellModel = new BooleanCellModel();
        booleanCellModel.setValue(entity.getState() != null && entity.getState() != ExpenseEntity.State.NOT_APPROVED);
        check.set(booleanCellModel);

        saveRowEventListener.accept(this);
    }


    public ObjectProperty<DateCellModel> dateProperty() {
        return date;
    }

    public void setDate(LocalDate newDate) {
        DateCellModel cellModel = date.get().clone();
        cellModel.resetError();

        if (newDate == null) {
            cellModel.setError(CellModel.CellError.EMPTY);
            date.set(cellModel);
            return;
        }

        if (Objects.equals(cellModel.getValue(), newDate)) {
            date.set(cellModel);
            return;
        }

        cellModel.setValue(newDate);
        cellModel.setWarning(CellModel.CellWarning.NO_SYNC);
        date.set(cellModel);

        save();
    }

    public ObjectProperty<StringCellModel> categoryProperty() {
        return category;
    }

    public void setCategory(String category) {
        StringCellModel cellModel = this.category.get().clone();
        cellModel.resetError();

        if (category == null || category.trim().isEmpty()) {
            cellModel.setError(CellModel.CellError.EMPTY);
            this.category.set(cellModel);
            return;
        }

        if (Objects.equals(cellModel.getValue(), category)) {
            this.category.set(cellModel);
            return;
        }

        if (!categoryEntityMap.containsKey(category)) {
            cellModel.setError(CellModel.CellError.NAME);
            this.category.set(cellModel);
            return;
        }

        cellModel.setValue(category);
        cellModel.setWarning(CellModel.CellWarning.NO_SYNC);
        this.category.set(cellModel);

        save();
    }

    public ObjectProperty<MoneyCellModel> costProperty() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        MoneyCellModel cellModel = this.cost.get().clone();
        cellModel.resetError();

        if (cost == null) {
            cellModel.setError(CellModel.CellError.EMPTY);
            this.cost.set(cellModel);
            return;
        }

        if (Objects.equals(cellModel.getValue(), cost)) {
            this.cost.set(cellModel);
            return;
        }

        cellModel.setValue(cost);
        cellModel.setWarning(CellModel.CellWarning.NO_SYNC);
        this.cost.set(cellModel);

        save();
    }

    public ObjectProperty<BooleanCellModel> checkProperty() {
        return check;
    }

    public void setCheck(Boolean check) {
        BooleanCellModel cellModel = this.check.get().clone();
        cellModel.resetError();

        if (Objects.equals(cellModel.getValue(), check)) {
            this.check.set(cellModel);
            return;
        }

        cellModel.setValue(check);
        cellModel.setWarning(CellModel.CellWarning.NO_SYNC);
        this.check.set(cellModel);

        save();
    }
}
