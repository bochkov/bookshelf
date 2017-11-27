package com.sb.bookshelf.fx;

import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public final class AnnotatedRow implements Callback<TableView, TableRow> {

    @Override
    public TableRow call(TableView param) {
        return new TableRow<Volume>() {
            @Override
            protected void updateItem(Volume volume, boolean empty) {
                super.updateItem(volume, empty);
                if (volume == null || volume.getAnnotation() == null
                        || volume.getAnnotation().isEmpty()) {
                    if (getStyleClass().contains("annotated"))
                        getStyleClass().remove("annotated");
                    if (!getStyleClass().contains("not-annotated"))
                        getStyleClass().add("not-annotated");
                } else {
                    if (getStyleClass().contains("not-annotated"))
                        getStyleClass().remove("not-annotated");
                    if (!getStyleClass().contains("annotated"))
                        getStyleClass().add("annotated");
                }
            }
        };
    }
}
