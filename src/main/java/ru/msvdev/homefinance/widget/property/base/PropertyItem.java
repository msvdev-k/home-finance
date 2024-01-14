package ru.msvdev.homefinance.widget.property.base;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import lombok.Setter;
import org.controlsfx.control.PropertySheet;

import java.util.Objects;
import java.util.Optional;


@Setter
public class PropertyItem<T> implements PropertySheet.Item {

    private final Class<T> type;
    private final ObjectProperty<T> value;

    private String category;
    private String name;
    private String description;
    private boolean editable = true;

    private Runnable changeValueListener;


    public PropertyItem(Class<T> type) {
        this.type = type;
        this.value = new SimpleObjectProperty<>();
    }


    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Object getValue() {
        return value.get();
    }

    @Override
    public void setValue(Object newValue) {
        if (newValue == null || Objects.equals(value.get(), newValue)) return;

        value.set(type.cast(newValue));

        if (changeValueListener != null) {
            changeValueListener.run();
        }
    }


    @Override
    public Optional<ObservableValue<?>> getObservableValue() {
        return Optional.of(value);
    }


    @Override
    public boolean isEditable() {
        return editable;
    }

}
