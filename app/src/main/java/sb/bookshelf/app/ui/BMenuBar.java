package sb.bookshelf.app.ui;

import javax.swing.*;

public final class BMenuBar extends JMenuBar {

    public BMenuBar(BMenu... items) {
        for (BMenu item : items) {
            add(item);
        }
    }
}
