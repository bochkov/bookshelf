package sb.bookshelf.app.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.*;

import net.miginfocom.swing.MigLayout;
import sb.bookshelf.common.model.Volume;

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

        var leftPanel = new JPanel(new MigLayout("wrap 2, insets 0, fillx", "[][fill, grow]"));
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

        var rightPanel = new JPanel(new MigLayout("wrap 1, insets 0, fill", "", "[top][fill, grow][top][fill, grow]"));
        rightPanel.add(new JLabel("Аннотация"));
        rightPanel.add(new JScrollPane(annotation), "grow");
        rightPanel.add(new JLabel("Книги в книге"));
        rightPanel.add(new JScrollPane(books), "grow");

        var cmdPanel = new JPanel(new MigLayout("nogrid, insets 0, center"));
        cmdPanel.add(new JButton(new AcSave("Сохранить")));
        cmdPanel.add(new JButton(new AcCancel("Отменить")));

        add(leftPanel);
        add(rightPanel);
        add(cmdPanel, "span");

        setPreferredSize(new Dimension(900, 500));
        pack();
        setLocationRelativeTo(owner);
    }

    public void edit(Volume vol) {
        if (vol != null) {
            bookTitle.setText(vol.getTitle());
            author.select(vol.getAuthor());
            publisher.select(vol.getPublisher());
            year.setText(vol.getYear());
            annotation.setText(vol.getAnnotation());
            isbn.setText(vol.getIsbn());
            pages.setValue(vol.getPages());
            books.setText(String.join("\n", vol.getBooks()));
            id.set(vol.getId());
        }
    }

    public Volume result() {
        if (result.get() == RESULT_OK) {
            var vol = new Volume();
            vol.setId(id.get());
            vol.setTitle(bookTitle.getText());
            vol.setAuthor(author.getSelectedItem());
            vol.setPublisher(publisher.getSelectedItem());
            vol.setYear(year.getText());
            vol.setAnnotation(annotation.getText());
            vol.setIsbn(isbn.getText());
            vol.setPages((Integer) pages.getValue());
            List<String> bookList = books.getText().isBlank() ?
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
