package ru.msvdev.homefinance.controller.utility.statistic.simple.property;

import lombok.Setter;
import org.controlsfx.control.PropertySheet;
import ru.msvdev.homefinance.widget.property.base.CollectionStringPropertyItem;
import ru.msvdev.homefinance.widget.property.base.LocalDatePropertyItem;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static ru.msvdev.homefinance.task.statistic.category.CategoryStatisticProcessor.ALL_CATEGORIES;

public class FilterPropertyContainer {

    private boolean enableListenerFlag = true;


    private final CollectionStringPropertyItem category;
    private final LocalDatePropertyItem fromDate;
    private final LocalDatePropertyItem toDate;
    private final CollectionStringPropertyItem period;

    @Setter
    private Consumer<FilterPropertyContainer> changePropertyListener;

    {
        category = new CollectionStringPropertyItem();
        category.setCategory("Фильтр");
        category.setName("Категория");
        category.setValue(ALL_CATEGORIES);
        category.setChangeValueListener(this::propertyChanged);

        fromDate = new LocalDatePropertyItem();
        fromDate.setCategory("Фильтр");
        fromDate.setName("Диапазон от");
        fromDate.setValue(LocalDate.of(2000, 1, 1));
        fromDate.setChangeValueListener(this::propertyChanged);

        toDate = new LocalDatePropertyItem();
        toDate.setCategory("Фильтр");
        toDate.setName("Диапазон до");
        toDate.setValue(LocalDate.now());
        toDate.setChangeValueListener(this::propertyChanged);

        period = new CollectionStringPropertyItem();
        period.setCategory("Фильтр");
        period.setName("Период");
        period.setValue(Period.YEAR.text);
        period.setCollection(Arrays.asList(Period.YEAR.text, Period.MONTH.text));
        period.setChangeValueListener(this::propertyChanged);
    }


    public void setEditor(PropertySheet editor) {
        editor.getItems().addAll(
                category,
                fromDate,
                toDate,
                period
        );
    }

    public void setCategories(List<String> categories) {
        categories.add(0, ALL_CATEGORIES);
        category.setCollection(categories);
    }


    public String getCategory() {
        return (String) category.getValue();
    }

    public void setCategory(String category) {
        enableListenerFlag = false;
        this.category.setValue(category);
        enableListenerFlag = true;
    }

    public LocalDate getFromDate() {
        return (LocalDate) fromDate.getValue();
    }

    public void setFromDate(LocalDate fromDate) {
        enableListenerFlag = false;
        this.fromDate.setValue(fromDate);
        enableListenerFlag = true;
    }

    public LocalDate getToDate() {
        return (LocalDate) toDate.getValue();
    }

    public void setToDate(LocalDate toDate) {
        enableListenerFlag = false;
        this.toDate.setValue(toDate);
        enableListenerFlag = true;
    }

    public Period getPeriod() {
        return Period.fromText((String) period.getValue());
    }

    public void setPeriod(Period period) {
        enableListenerFlag = false;
        this.period.setValue(period.text);
        enableListenerFlag = true;
    }

    private void propertyChanged() {
        if (enableListenerFlag && changePropertyListener != null) {
            changePropertyListener.accept(this);
        }
    }

    public enum Period {

        MONTH("Месяц"),
        YEAR("Год");

        public final String text;

        Period(String text) {
            this.text = text;
        }

        public static Period fromText(String text) {
            if (text.equalsIgnoreCase("Месяц")) {
                return MONTH;
            }
            return YEAR;
        }
    }
}
