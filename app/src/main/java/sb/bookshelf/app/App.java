package sb.bookshelf.app;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.*;

import com.formdev.flatlaf.FlatLightLaf;
import kong.unirest.core.HeaderNames;
import kong.unirest.core.MimeTypes;
import kong.unirest.core.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sb.bdev.ui.swing.HMenu;
import sb.bdev.ui.swing.HMenuBar;
import sb.bdev.ui.swing.HMenuItem;
import sb.bookshelf.app.services.CountVolumes;
import sb.bookshelf.app.services.LoadMetadata;
import sb.bookshelf.app.services.LoadVolumes;
import sb.bookshelf.app.ui.*;

@Slf4j
public final class App extends JFrame {

    public static final String TITLE = "Bookshelf";

    public App() {
        setTitle(TITLE);
        setIconImage(Images.LOGO.getImage());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));

        setJMenuBar(new HMenuBar(
                new HMenu("Файл",
                        new HMenuItem(new OpenProps("Настройки"))
                )
        ));

        var books = new BookPanel(this);
        add(books);

        pack();
        setLocationRelativeTo(null);
        addWindowListener(new WndAdapt(books));
    }

    @RequiredArgsConstructor
    private static final class WndAdapt extends WindowAdapter {

        private final BookPanel books;

        @Override
        public void windowOpened(WindowEvent e) {
            new CountVolumes(books).start();
            new LoadMetadata(books, books).start();
            new LoadVolumes(books).start();
        }

        @Override
        public void windowClosing(WindowEvent e) {
            Unirest.shutDown();
            AppProps.store();
        }
    }

    private static final class OpenProps extends AbstractAction {

        public OpenProps(String text) {
            super(text);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                AppProps.store();
                Desktop.getDesktop().open(AppProps.PROP_FILE);
            } catch (IOException ex) {
                LOG.warn(ex.getMessage(), ex);
            }
        }
    }

    public static void main(String[] args) {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            LOG.warn(ex.getMessage());
        }
        AppProps.load();
        Unirest.config()
                .defaultBaseUrl(AppProps.prop(AppProps.HOST))
                .addDefaultHeader(HeaderNames.ACCEPT, MimeTypes.JSON)
                .addDefaultHeader(HeaderNames.CONTENT_TYPE, MimeTypes.JSON);
        String user = AppProps.prop(AppProps.USER, "");
        String password = AppProps.prop(AppProps.PASSWORD, "");
        if (!user.isEmpty() || !password.isEmpty())
            Unirest.config().setDefaultBasicAuth(user, password);
        if (Taskbar.getTaskbar().isSupported(Taskbar.Feature.ICON_IMAGE))
            Taskbar.getTaskbar().setIconImage(Images.LOGO.getImage());
        if (Desktop.getDesktop().isSupported(Desktop.Action.APP_ABOUT)) {
            Desktop.getDesktop().setAboutHandler(new AboutDialog());
        }
        new App().setVisible(true);
    }
}
