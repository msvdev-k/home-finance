package ru.msvdev.homefinance.widget.table.category.columnbuilder;

import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import ru.msvdev.homefinance.widget.table.category.CategoryRowModel;


public class IdColumnBuilder {

    private String name = "ID";
    private double prefWidth = 32.0;

    public void setName(String name) {
        this.name = name;
    }

    public void setPrefWidth(double prefWidth) {
        this.prefWidth = prefWidth;
    }

    public TableColumn<CategoryRowModel, Integer> build() {
        TableColumn<CategoryRowModel, Integer> column = new TableColumn<>(name);
        column.setPrefWidth(prefWidth);

        column.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        column.setCellFactory(c -> new TableCell<CategoryRowModel, Integer>() {
            {
                setAlignment(Pos.TOP_CENTER);
            }

            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.toString());
                }
            }
        });
        column.setEditable(false);

        return column;
    }

}
