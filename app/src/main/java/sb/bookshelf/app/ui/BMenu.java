package sb.bookshelf.app.ui;

import javax.swing.*;

public final class BMenu extends JMenu {

    public BMenu(String title, BMenuItem... items) {
        setText(title);
        for (BMenuItem item : items)
            add(item);
    }
}
