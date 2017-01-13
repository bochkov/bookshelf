package com.sergeybochkov.bookshelf.fx.graphic;

import com.sergeybochkov.bookshelf.fx.model.Volume;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.util.Callback;
import javafx.util.Duration;

import java.lang.reflect.Field;

public final class TableTooltip implements Callback<TableView<Volume>, TableRow<Volume>> {

    private void hackTooltipStartTiming(Tooltip tooltip) {
        try {
            Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
            fieldBehavior.setAccessible(true);
            Object objBehavior = fieldBehavior.get(tooltip);

            Field fieldTimer = objBehavior.getClass().getDeclaredField("hideTimer");
            fieldTimer.setAccessible(true);
            Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

            objTimer.getKeyFrames().clear();
            objTimer.getKeyFrames().add(new KeyFrame(new Duration(200000)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public TableRow<Volume> call(TableView<Volume> param) {
        Tooltip tt = new Tooltip();
        return new TableRow<Volume>() {
            @Override
            protected void updateItem(Volume volume, boolean empty) {
                super.updateItem(volume, empty);
                if (volume == null || volume.getAnnotation() == null || volume.getAnnotation().equals("")) {
                    if (getStyleClass().contains("annotated"))
                        getStyleClass().remove("annotated");
                    if (!getStyleClass().contains("not-annotated"))
                        getStyleClass().add("not-annotated");
                } else {
                    tt.setText(volume.getAnnotation());
                    tt.setWrapText(true);
                    hackTooltipStartTiming(tt);
                    tt.setPrefWidth(400D);
                    setTooltip(tt);
                    if (getStyleClass().contains("not-annotated"))
                        getStyleClass().remove("not-annotated");
                    if (!getStyleClass().contains("annotated"))
                        getStyleClass().add("annotated");
                }
            }
        };
    }

}
