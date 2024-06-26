package sb.bookshelf.app.ui;

import net.miginfocom.swing.MigLayout;
import sb.bdev.ui.HotKey;
import sb.bookshelf.common.model.VolumeInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class AddDialog extends JDialog {

    public static final int RESULT_OK = 0;
    public static final int RESULT_CANCEL = 1;

    private final AtomicInteger result = new AtomicInteger(RESULT_CANCEL);

    private final AtomicReference<String> id = new AtomicReference<>();
    private final JTextField bookTitle = new JTextField();
    private final BComboBox<String> author = new BComboBox<>(true, String.class);
    private final BComboBox<String> publisher = new BComboBox<>(true, String.class);
    private final JTextField year = new JTextField();
    private final JTextArea annotation = new JTextArea();
    private final JTextField isbn = new JTextField();
    private final JSpinner pages = new JSpinner(new SpinnerNumberModel(0, 0, null, 2));
    private final JTextArea books = new JTextArea();

    public AddDialog(Frame owner, String frmTitle, List<String> authors, List<String> publishers) {
        super(owner, frmTitle, true);

        setLayout(new MigLayout("wrap 2", "[fill, grow, 40%][fill, grow, 60%]", "[fill, grow][]"));

        JPanel leftPanel = new JPanel(new MigLayout("wrap 2, insets 0, fillx", "[][fill, grow]"));
        leftPanel.add(new JLabel("Название"));
        leftPanel.add(bookTitle);
        leftPanel.add(new JLabel("Автор"));
        leftPanel.add(author);
        author.addItems(authors, true);
        leftPanel.add(new JLabel("Издательство"));
        leftPanel.add(publisher);
        publisher.addItems(publishers, true);
        leftPanel.add(new JLabel("Год издания"));
        leftPanel.add(year);
        leftPanel.add(new JLabel("ISBN"));
        leftPanel.add(isbn);
        leftPanel.add(new JLabel("Кол-во страниц"));
        leftPanel.add(pages);

        JPanel rightPanel = new JPanel(new MigLayout("wrap 1, insets 0, fill", "", "[top][fill, grow][top][fill, grow]"));
        rightPanel.add(new JLabel("Аннотация"));
        rightPanel.add(new JScrollPane(annotation), "grow");
        rightPanel.add(new JLabel("Книги в книге"));
        rightPanel.add(new JScrollPane(books), "grow");

        JPanel cmdPanel = new JPanel(new MigLayout("nogrid, insets 0, center"));
        cmdPanel.add(new JButton(new AcSave("Сохранить")));
        cmdPanel.add(new JButton(new AcCancel("Отменить")));
        new HotKey(KeyEvent.VK_ESCAPE, new AcCancel("Cancel")).on(getRootPane());

        add(leftPanel);
        add(rightPanel);
        add(cmdPanel, "span");

        setPreferredSize(new Dimension(900, 500));
        pack();
        setLocationRelativeTo(owner);
    }

    public void edit(VolumeInfo vol) {
        if (vol != null) {
            bookTitle.setText(vol.getTitle());
            author.select(vol.getAuthor());
            publisher.select(vol.getPublisher());
            year.setText(vol.getYear());
            annotation.setText(vol.getAnnotation());
            isbn.setText(vol.getIsbn());
            pages.setValue(vol.getPages());
            books.setText(vol.printBooks());
            id.set(vol.getId());
        }
    }

    public VolumeInfo result() {
        if (result.get() == RESULT_OK) {
            var vol = new VolumeInfo();
            vol.setId(id.get());
            vol.setTitle(bookTitle.getText());
            vol.setAuthor(author.getSelectedItem());
            vol.setPublisher(publisher.getSelectedItem());
            vol.setYear(year.getText());
            vol.setAnnotation(annotation.getText());
            vol.setIsbn(isbn.getText());
            vol.setPages((Integer) pages.getValue());
            List<String> bookList = books.getText().isEmpty() ?
                    new ArrayList<>() :
                    List.of(books.getText().split("\n"));
            vol.setBooks(bookList);
            return vol;
        } else
            return null;
    }

    private final class AcCancel extends AbstractAction {

        public AcCancel(String text) {
            super(text);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            result.set(RESULT_CANCEL);
            id.set(null);
            dispose();
        }
    }

    private final class AcSave extends AbstractAction {

        public AcSave(String text) {
            super(text);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            result.set(RESULT_OK);
            dispose();
        }
    }

}
