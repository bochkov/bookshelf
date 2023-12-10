package sb.bookshelf.app.ui;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;
import sb.bookshelf.app.Images;
import sb.bookshelf.app.services.SearchVolumes;
import sb.bookshelf.common.model.Volume;

@Slf4j
public final class SearchPanel extends JPanel {

    private final VolumeTableModel model;
    private final List<Volume> origin;
    private final List<Volume> searched;

    private final JTextField field = new JTextField();

    public SearchPanel(VolumeTableModel model) {
        this.model = model;
        this.origin = new ArrayList<>();
        this.searched = new ArrayList<>();

        setLayout(new MigLayout("nogrid, fillx, insets 0"));
        add(field, "grow");
        AcSearch acSearch = new AcSearch(Images.ICON_FIND);
        add(new JButton(acSearch));
        add(new JButton(new AcClear(Images.ICON_CLEAR)));

        this.field.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "startSearch");
        this.field.getActionMap().put("startSearch", acSearch);
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
