package ru.msvdev.homefinance.viewutils.table;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import ru.msvdev.homefinance.viewutils.table.cell.*;
import ru.msvdev.homefinance.viewutils.table.converter.StringConverter;

import java.time.LocalDate;


public class BaseTableCell<S, T extends CellModel<?>> extends TableCell<S, T> {

    private final static String ERROR_STYLE = "-fx-text-fill: rgb(156,0,6); -fx-background-color: rgb(255,199,206);";
    private final static String WARNING_STYLE = "-fx-text-fill: rgb(156,101,0); -fx-background-color: rgb(255,235,156);";

    private Node editNode;
    StringConverter<T> converter;


    public BaseTableCell(StringConverter<T> converter) {
        this.converter = converter;
    }

    private Node getTextField() {
        TextField textField = new TextField();

        textField.setOnAction(event -> {
            commitEdit(converter.fromString(textField.getText()));
            event.consume();
        });

        textField.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
                event.consume();
            }
        });

        return textField;
    }

    private Node getDatePicker() {
        DatePicker datePicker = new DatePicker(LocalDate.now());

        datePicker.setOnAction(event -> {
            commitEdit(converter.fromString(datePicker.getValue().toString()));
            event.consume();
        });

        datePicker.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
                event.consume();
            }
        });

        return datePicker;
    }

    private Node getEditor() {
        T item = getItem();

        if (item instanceof DateCellModel) {
            if (editNode == null) {
                editNode = getDatePicker();
            }

            DatePicker datePicker = (DatePicker) editNode;
            datePicker.setValue((LocalDate) item.getValue());
            datePicker.getEditor().selectAll();

            return datePicker;
        }

        if (editNode == null) {
            editNode = getTextField();
        }

        TextField textField = (TextField) editNode;
        textField.setText(converter.toString(item));
        textField.selectAll();

        return textField;
    }


    private void resetView() {
        setText(null);
        setGraphic(null);
        setStyle(null);
        setTooltip(null);
    }


    @Override
    public void startEdit() {
        if (!isEditable() || !getTableView().isEditable() || !getTableColumn().isEditable()) {
            return;
        }
        super.startEdit();

        if (isEditing()) {
            resetView();

            Node editor = getEditor();
            setGraphic(editor);
            editor.requestFocus();
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        resetView();
        setText(converter.toString(getItem()));
    }


    @Override
    public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        resetView();

        if (empty || item == null) {
            return;
        }

        if (item.isError()) {
            setText(item.getErrorText());
            setStyle(ERROR_STYLE);
            setTooltip(new Tooltip(item.getErrorDescription()));
            setAlignment(Pos.TOP_CENTER);
            return;
        }

        if (item.isWarning()) {
            setStyle(WARNING_STYLE);
            setTooltip(new Tooltip(item.getWarningDescription()));
        }

        setText(converter.toString(item));

        if (item instanceof IntegerCellModel ||
                item instanceof DoubleCellModel ||
                item instanceof MoneyCellModel) {
            setAlignment(Pos.TOP_RIGHT);

        } else if (item instanceof StringCellModel) {
            setAlignment(Pos.TOP_LEFT);

        } else if (item instanceof DateCellModel) {
            setAlignment(Pos.TOP_CENTER);
        }
    }
}
