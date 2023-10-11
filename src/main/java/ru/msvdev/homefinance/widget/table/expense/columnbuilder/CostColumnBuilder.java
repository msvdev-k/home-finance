package ru.msvdev.homefinance.widget.table.expense.columnbuilder;

import ru.msvdev.homefinance.viewutils.table.BaseColumnBuilder;
import ru.msvdev.homefinance.viewutils.table.cell.MoneyCellModel;
import ru.msvdev.homefinance.viewutils.table.converter.MoneyStringConverter;
import ru.msvdev.homefinance.widget.table.expense.ExpenseRowModel;
import ru.msvdev.homefinance.widget.table.expense.editevent.CostEditEvent;
import ru.msvdev.homefinance.widget.table.expense.valuefactory.CostCellValueFactory;


public class CostColumnBuilder extends BaseColumnBuilder<ExpenseRowModel, MoneyCellModel> {

    public CostColumnBuilder() {
        setName("Стоимость");
        setPrefWidth(130);
        setEditable(true);
        setCellValueFactory(new CostCellValueFactory());
        setConverter(new MoneyStringConverter());
        setEditEvent(new CostEditEvent());
    }
}
