package sb.bookshelf.app.ui;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;
import sb.bookshelf.common.model.VolumeInfo;

public final class BookInfo extends JPanel {

    private static final String AUTHOR_STR = "<html><font>%s";
    private static final String TITLE_STR = "<html><font size=5>%s";
    private static final String META_STR = "<html><font size=3 color=gray>%s";

    private final JLabel title = new JLabel();
    private final JLabel author = new JLabel();
    private final JLabel metaData = new JLabel();
    private final JTextArea annotation = new JTextArea();
    private final JScrollPane scAnnotation = new JScrollPane(annotation);
    private final JTextArea books = new JTextArea();
    private final JScrollPane scBooks = new JScrollPane(books);

    public BookInfo() {
        setLayout(new MigLayout(
                "flowy, gap 1, insets 0, fillx, hidemode 3",
                "",
                "[][][]15[fill, grow]10[fill, grow]"
        ));
        add(author);
        add(title);
        add(metaData);
        add(scAnnotation, "grow");
        annotation.setEditable(false);
        add(scBooks, "grow");
        books.setEditable(false);
        scAnnotation.setVisible(false);
        scBooks.setVisible(false);
    }

    public void view(VolumeInfo volume) {
        if (volume == null)
            clear();
        else {
            title.setText(String.format(TITLE_STR, volume.getTitle()));
            author.setText(String.format(AUTHOR_STR, volume.getAuthor()));
            metaData.setText(String.format(META_STR, volume.metaData()));
            annotation.setText(volume.getAnnotation());
            books.setText(volume.printBooks());
            scAnnotation.setVisible(!annotation.getText().isEmpty());
            scBooks.setVisible(!books.getText().isEmpty());
        }
    }

    private void clear() {
        title.setText("");
        author.setText("");
        metaData.setText("");
        annotation.setText("");
        books.setText("");
        scAnnotation.setVisible(false);
        scBooks.setVisible(false);
    }
}
