package sb.bookshelf.app.ui;

import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;
import sb.bdev.ui.HotKey;
import sb.bookshelf.app.Images;
import sb.bookshelf.app.services.SearchVolumes;
import sb.bookshelf.common.model.VolumeInfo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public final class SearchPanel extends JPanel {

    private final VolumeTableModel model;
    private final List<VolumeInfo> origin;
    private final List<VolumeInfo> searched;

    private final JTextField field = new JTextField();

    public SearchPanel(VolumeTableModel model) {
        this.model = model;
        this.origin = new ArrayList<>();
        this.searched = new ArrayList<>();

        setLayout(new MigLayout("nogrid, fillx, insets 0"));
        add(field, "grow");

        AcSearch acSearch = new AcSearch(Images.ICON_FIND);
        add(new JButton(acSearch));
        new HotKey(KeyEvent.VK_ENTER, acSearch).on(field);

        AcClear acClear = new AcClear(Images.ICON_CLEAR);
        add(new JButton(acClear));
        new HotKey(KeyEvent.VK_ESCAPE, acClear).on(field);
    }

    private final class AcSearch extends AbstractAction {

        public AcSearch(Icon icon) {
            super("", icon);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (origin.isEmpty())
                origin.addAll(model.allItems());
            new SearchVolumes(field.getText(), resp -> {
                if (resp.isSuccess()) {
                    searched.clear();
                    searched.addAll(resp.getBody());
                    model.set(searched);
                }
            }).start();
        }
    }

    private final class AcClear extends AbstractAction {

        public AcClear(Icon icon) {
            super("", icon);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            field.setText("");
            model.set(origin);
        }
    }
}
