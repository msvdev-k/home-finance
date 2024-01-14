package ru.msvdev.homefinance.widget.property.base;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import lombok.Getter;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.AbstractPropertyEditor;
import org.controlsfx.property.editor.PropertyEditor;

import java.util.Collection;
import java.util.Optional;


@Getter
public class CollectionStringPropertyItem extends PropertyItem<String> {

    private final ComboBox<String> categoriesComboBox = new ComboBox<>();


    public CollectionStringPropertyItem() {
        super(String.class);
    }


    public void setCollection(Collection<String> collection) {
        categoriesComboBox.setItems(
                FXCollections.observableArrayList(collection)
        );
    }


    @Override
    public Optional<Class<? extends PropertyEditor<?>>> getPropertyEditorClass() {
        return Optional.of(CollectionPropertyEditor.class);
    }


    public static class CollectionPropertyEditor extends AbstractPropertyEditor<String, ComboBox<String>> {

        public CollectionPropertyEditor(PropertySheet.Item property) {
            super(property, ((CollectionStringPropertyItem) property).getCategoriesComboBox());
        }

        @Override
        protected ObservableValue<String> getObservableValue() {
            return getEditor().getSelectionModel().selectedItemProperty();
        }

        @Override
        public void setValue(String value) {
            getEditor().getSelectionModel().select(value);
        }

    }

}
