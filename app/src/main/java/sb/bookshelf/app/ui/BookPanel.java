package sb.bookshelf.app.ui;

import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;
import sb.bdev.ui.HotKey;
import sb.bookshelf.app.App;
import sb.bookshelf.app.Images;
import sb.bookshelf.app.services.CountVolumes;
import sb.bookshelf.app.services.DeleteVolume;
import sb.bookshelf.app.services.SaveVolume;
import sb.bookshelf.common.messages.DeleteResponse;
import sb.bookshelf.common.model.VolumeInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public final class BookPanel extends JPanel implements AuthorListener, PublisherListener {

    private final Frame owner;
    private final VolumeTableModel model = new VolumeTableModel();
    private final JTable table = new JTable(model);
    private final BookInfo bookInfo = new BookInfo();
    private final JLabel countLabel = new JLabel();

    private final List<String> authorsCache = new ArrayList<>();
    private final List<String> publishersCache = new ArrayList<>();

    public BookPanel(Frame owner) {
        this.owner = owner;
        setLayout(new MigLayout("wrap 2, fill", "[40%,fill]20[60%,fill]", "fill, top"));

        JPanel listPanel = new JPanel(new MigLayout("wrap 1, fill, insets 0", "fill", "[top][grow][top]"));

        SearchPanel searchField = new SearchPanel(model);
        listPanel.add(searchField);
        table.setTableHeader(null);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                VolumeInfo vol = model.get(table.getSelectedRow());
                bookInfo.view(vol);
            }
        });
        listPanel.add(new JScrollPane(table), "grow");

        JPanel cmdPanel = new JPanel(new MigLayout("insets 0", "[][][]push[]"));
        Action acAdd = new AcAdd(Images.ICON_ADD);
        cmdPanel.add(new JButton(acAdd));
        new HotKey(KeyEvent.VK_INSERT, acAdd).on(this);

        Action acEdit = new AcEdit(Images.ICON_EDIT);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2)
                    acEdit.actionPerformed(null);
            }
        });
        cmdPanel.add(new JButton(acEdit));

        Action acDel = new AcDelete(Images.ICON_DEL);
        cmdPanel.add(new JButton(acDel));
        new HotKey(KeyEvent.VK_DELETE, new AcDelete(null)).on(this);

        cmdPanel.add(countLabel);
        listPanel.add(cmdPanel);

        add(listPanel, "grow");
        add(bookInfo);
    }

    public void fill(List<VolumeInfo> volumes) {
        model.set(volumes);
    }

    public void count(Long total) {
        countLabel.setText("Всего томов: " + total);
    }

    @Override
    public void updateAuthors(List<String> authors) {
        authorsCache.clear();
        authorsCache.addAll(authors);
    }

    @Override
    public void updatePublishers(List<String> publishers) {
        publishersCache.clear();
        publishersCache.addAll(publishers);
    }

    private void addAuthor(String author) {
        if (!authorsCache.contains(author))
            authorsCache.add(author);
        Collections.sort(authorsCache);
    }

    private void addPublisher(String publisher) {
        if (!publishersCache.contains(publisher))
            publishersCache.add(publisher);
        Collections.sort(publishersCache);
    }

    private void showError(int statusCode) {
        String text;
        if (statusCode == 403) {
            text = "Операция не разрешена";
        } else {
            text = "Ошибка выполнения";
        }
        JOptionPane.showMessageDialog(this, text, App.TITLE, JOptionPane.ERROR_MESSAGE);
    }

    private final class AcAdd extends AbstractAction {

        public AcAdd(Icon icon) {
            super("", icon);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            AddDialog dlg = new AddDialog(owner, "Добавить", authorsCache, publishersCache);
            dlg.setVisible(true);
            VolumeInfo vol = dlg.result();
            if (vol != null) {
                new SaveVolume(vol, response -> {
                    if (response.isSuccess()) {
                        VolumeInfo v = response.getBody();
                        model.put(0, v);
                        new CountVolumes(BookPanel.this).start();
                        addAuthor(v.getAuthor());
                        addPublisher(v.getPublisher());
                    } else {
                        showError(response.getStatus());
                    }
                }).start();
            }
        }
    }

    private final class AcEdit extends AbstractAction {

        public AcEdit(Icon icon) {
            super("", icon);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            AddDialog dlg = new AddDialog(owner, "Редактировать", authorsCache, publishersCache);
            dlg.edit(model.get(table.getSelectedRow()));
            dlg.setVisible(true);
            VolumeInfo vol = dlg.result();
            if (vol != null) {
                new SaveVolume(vol, response -> {
                    if (response.isSuccess()) {
                        VolumeInfo v = response.getBody();
                        model.replaceIds(v);
                        new CountVolumes(BookPanel.this).start();
                        addAuthor(v.getAuthor());
                        addPublisher(v.getPublisher());
                    } else {
                        showError(response.getStatus());
                    }
                }).start();
            }
        }
    }

    private final class AcDelete extends AbstractAction {

        public AcDelete(Icon icon) {
            super("", icon);
        }

        private List<VolumeInfo> selectedVolumes() {
            List<VolumeInfo> volumes = new ArrayList<>();
            for (int i : table.getSelectedRows())
                volumes.add(model.get(i));
            return volumes;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            List<VolumeInfo> volumes = selectedVolumes();
            if (volumes.isEmpty())
                return;

            String msg = volumes.size() > 1 ?
                    String.format("Удалить %s шт?", volumes.size()) :
                    String.format("Удалить '%s'?", volumes.get(0).desc());
            int res = JOptionPane.showOptionDialog(BookPanel.this, msg, "Подтверждение", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет"}, "Нет");
            if (res == JOptionPane.YES_OPTION) {
                new DeleteVolume(
                        volumes.stream().map(VolumeInfo::getId).toList(),
                        resp -> {
                            if (resp.isSuccess()) {
                                DeleteResponse info = resp.getBody();
                                model.removeIds(info.getIds());
                                new CountVolumes(BookPanel.this).start();
                            } else {
                                showError(resp.getStatus());
                            }
                        }
                ).start();
            }
        }
    }

}
