package ru.msvdev.homefinance.widget.table.category;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import ru.msvdev.homefinance.data.entity.CategoryEntity;
import ru.msvdev.homefinance.task.data.category.DeleteCategoryByIdTaskBuilder;
import ru.msvdev.homefinance.task.data.category.FindAllCategoriesTaskBuilder;
import ru.msvdev.homefinance.task.operation.TaskBuilder;
import ru.msvdev.homefinance.viewutils.table.TableController;
import ru.msvdev.homefinance.viewutils.table.cell.StringCellModel;
import ru.msvdev.homefinance.widget.table.category.columnbuilder.CategoryColumnBuilder;
import ru.msvdev.homefinance.widget.table.category.columnbuilder.DescriptionColumnBuilder;
import ru.msvdev.homefinance.widget.table.category.columnbuilder.IdColumnBuilder;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;


public class CategoryTableController extends TableController<CategoryRowModel, Integer> {

    private TableColumn<CategoryRowModel, StringCellModel> categoryColumn;
    private final Set<String> categories = ConcurrentHashMap.newKeySet();


    public CategoryTableController(TaskBuilder taskBuilder) {
        super(taskBuilder);
    }


    @Override
    public void initTable() {
        super.initTable();
        categories.clear();

        tableView.getSelectionModel().setCellSelectionEnabled(true);
        tableView.setEditable(true);


        IdColumnBuilder idColumnBuilder = new IdColumnBuilder();
        TableColumn<CategoryRowModel, Integer> idColumn = idColumnBuilder.build();

        CategoryColumnBuilder categoryColumnBuilder = new CategoryColumnBuilder();
        categoryColumn = categoryColumnBuilder.build();

        DescriptionColumnBuilder descriptionColumnBuilder = new DescriptionColumnBuilder();
        TableColumn<CategoryRowModel, StringCellModel> descriptionColumn = descriptionColumnBuilder.build();


        tableView.getColumns().add(idColumn);
        tableView.getColumns().add(categoryColumn);
        tableView.getColumns().add(descriptionColumn);
    }


    private CategoryRowModel getNewRow() {
        CategoryRowModel rowModel = CategoryRowModel.getInstance();

        rowModel.setCategories(categories);
        rowModel.setTaskBuilder(taskBuilder);
        rowModel.setRowSaveListener(this::saveRowListener);

        return rowModel;
    }

    private void listCategoryEntityListener(List<CategoryEntity> categoryEntities) {
        ObservableList<CategoryRowModel> items = tableView.getItems();

        for (CategoryEntity entity : categoryEntities) {
            CategoryRowModel rowModel = getNewRow();

            rowModel.idProperty().set(entity.getId());
            rowModel.categoryProperty().get().setValue(entity.getName());
            rowModel.categoryProperty().get().resetError();
            rowModel.descriptionProperty().get().setValue(entity.getDescription());

            rows.put(entity.getId(), rowModel);
            categories.add(entity.getName());

            items.add(rowModel);
        }
    }

    private void saveRowListener(CategoryRowModel rowModel) {
        if (rowModel == newRow) {
            newRow = null;
            rows.put(rowModel.idProperty().get(), rowModel);
        }
    }

    @Override
    public void refresh() {
        super.refresh();
        categories.clear();

        FindAllCategoriesTaskBuilder builder = taskBuilder.getBuilder(FindAllCategoriesTaskBuilder.class);
        builder.addSucceededListener(this::listCategoryEntityListener);
        builder.buildAndRun();
    }

    @Override
    public void newRow() {
        if (newRow == null) {
            newRow = getNewRow();
            tableView.getItems().add(newRow);
        }

        tableView.getSelectionModel().select(newRow);
        int index = tableView.getSelectionModel().getSelectedIndex();
        tableView.getSelectionModel().select(index, categoryColumn);
        tableView.requestFocus();
    }

    @Override
    public void removeSelected() {
        ObservableList<CategoryRowModel> selectedItems = tableView.getSelectionModel().getSelectedItems();
        DeleteCategoryByIdTaskBuilder builder = taskBuilder.getBuilder(DeleteCategoryByIdTaskBuilder.class);

        for (CategoryRowModel item : selectedItems) {
            if (item == newRow) {
                newRow = null;
                tableView.getItems().remove(item);
                continue;
            }

            builder.setId(item.idProperty().get());
            builder.addSucceededListener(new DeleteItemListener(item));
            builder.buildAndRun();
        }
    }


    private class DeleteItemListener implements Consumer<Void> {
        private final CategoryRowModel rowModel;

        public DeleteItemListener(CategoryRowModel rowModel) {
            this.rowModel = rowModel;
        }

        @Override
        public void accept(Void unused) {
            tableView.getItems().remove(rowModel);
            rows.remove(rowModel.idProperty().get());
            categories.remove(rowModel.categoryProperty().get().getValue());
        }
    }
}
