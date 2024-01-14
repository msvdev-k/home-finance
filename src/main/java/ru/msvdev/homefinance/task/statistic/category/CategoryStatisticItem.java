package ru.msvdev.homefinance.task.statistic.category;

import javafx.beans.property.StringProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryStatisticItem {

    private StringProperty period;
    private StringProperty cost;

}
