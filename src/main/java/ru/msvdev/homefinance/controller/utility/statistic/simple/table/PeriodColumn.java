package ru.msvdev.homefinance.controller.utility.statistic.simple.table;

import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import ru.msvdev.homefinance.task.statistic.category.CategoryStatisticItem;

public class PeriodColumn extends TableColumn<CategoryStatisticItem, String> {

    {
        setText("Период");
        setPrefWidth(110);
        setSortable(false);

        setCellValueFactory(param -> param.getValue().getPeriod());
        setCellFactory(new Callback<TableColumn<CategoryStatisticItem, String>, TableCell<CategoryStatisticItem, String>>() {
            @Override
            public TableCell<CategoryStatisticItem, String> call(TableColumn<CategoryStatisticItem, String> param) {
                return new TableCell<CategoryStatisticItem, String>() {

                    {
                        setAlignment(Pos.TOP_CENTER);
                    }

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(item);
                        }
                    }
                };
            }
        });
    }

}
