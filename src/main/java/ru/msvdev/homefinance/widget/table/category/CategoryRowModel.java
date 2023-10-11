package ru.msvdev.homefinance.widget.table.category;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import ru.msvdev.homefinance.data.entity.CategoryEntity;
import ru.msvdev.homefinance.task.data.category.NewCategoryTaskBuilder;
import ru.msvdev.homefinance.task.data.category.UpdateCategoryTaskBuilder;
import ru.msvdev.homefinance.viewutils.table.cell.CellModel;
import ru.msvdev.homefinance.viewutils.table.cell.StringCellModel;
import ru.msvdev.homefinance.viewutils.table.RowModel;

import java.util.Objects;
import java.util.Set;


public class CategoryRowModel extends RowModel<CategoryRowModel, Integer> {

    private Set<String> categories;

    private final ObjectProperty<StringCellModel> category;
    private final ObjectProperty<StringCellModel> description;


    {
        category = new SimpleObjectProperty<>();
        description = new SimpleObjectProperty<>();
    }


    private CategoryRowModel() {
    }


    public static CategoryRowModel getInstance() {
        CategoryRowModel categoryRowModel = new CategoryRowModel();

        StringCellModel category = new StringCellModel();
        category.setError(CellModel.CellError.REQUIRED);
        categoryRowModel.category.set(category);

        categoryRowModel.description.set(new StringCellModel());

        return categoryRowModel;
    }


    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }


    private void save() {
        if (id.get() == null) {
            NewCategoryTaskBuilder builder = taskBuilder.getBuilder(NewCategoryTaskBuilder.class);

            builder.setName(category.get().getValue());
            builder.setDescription(description.get().getValue());
            builder.addSucceededListener(this::succeededListener);
            builder.buildAndRun();

        } else {
            UpdateCategoryTaskBuilder builder = taskBuilder.getBuilder(UpdateCategoryTaskBuilder.class);

            builder.setId(id.get());
            builder.setName(category.get().getValue());
            builder.setDescription(description.get().getValue());
            builder.addSucceededListener(this::succeededListener);
            builder.buildAndRun();
        }
    }

    private void succeededListener(CategoryEntity entity) {
        id.set(entity.getId());

        StringCellModel categoryCellModel = new StringCellModel();
        categoryCellModel.setValue(entity.getName());
        category.setValue(categoryCellModel);

        StringCellModel descriptionCellModel = new StringCellModel();
        descriptionCellModel.setValue(entity.getDescription());
        description.setValue(descriptionCellModel);

        saveRowEventListener.accept(this);
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

        if (categories.contains(category)) {
            cellModel.setError(CellModel.CellError.UNIQUE);
            this.category.set(cellModel);
            return;
        }

        if (cellModel.getValue() != null) {
            categories.remove(cellModel.getValue());
        }
        categories.add(category);
        cellModel.setValue(category);
        cellModel.setWarning(CellModel.CellWarning.NO_SYNC);

        this.category.set(cellModel);

        this.save();
    }

    public ObjectProperty<StringCellModel> descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        StringCellModel cellModel = this.description.get().clone();

        if (Objects.equals(cellModel.getValue(), description)) {
            return;
        }

        cellModel.setValue(description);
        cellModel.setWarning(CellModel.CellWarning.NO_SYNC);
        this.description.set(cellModel);

        if (!this.category.get().isError()) {
            this.save();
        }
    }

}
