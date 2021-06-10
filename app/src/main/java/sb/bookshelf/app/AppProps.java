package sb.bookshelf.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class AppProps extends Properties {

    public static final String HOST = "server.host";
    public static final String USER = "server.username";
    public static final String PASSWORD = "server.password";

    public static final File PROP_FILE = new File(System.getProperty("user.home") + File.separator + ".bookshelf", "bookshelf");

    private static AppProps instance;

    private AppProps() {
    }

    public static AppProps getInstance() {
        if (instance == null)
            instance = new AppProps();
        return instance;
    }

    public static void load() {
        try (var fis = new FileInputStream(PROP_FILE)) {
            AppProps.getInstance().load(fis);
            LOG.info("loaded props {}", instance);
        } catch (IOException ex) {
            AppProps.getInstance().put(HOST, "http://localhost:8080");
            AppProps.getInstance().put(USER, "");
            AppProps.getInstance().put(PASSWORD, "");
            LOG.info("cannot load property file. using default one");
        }
    }

    public static void store() {
        var parent = PROP_FILE.getParentFile();
        try {
            if (!parent.exists())
                Files.createDirectory(parent.toPath());
        } catch (IOException ex) {
            LOG.info(ex.getMessage(), ex);
            return;
        }
        try (var fout = new FileOutputStream(PROP_FILE)) {
            AppProps.getInstance().store(fout, "");
            LOG.info("stored props {}", instance);
        } catch (IOException ex) {
            LOG.warn("cannot store property file");
        }
    }

    public static String prop(String key) {
        return instance.getProperty(key);
    }

    public static <T> T prop(String key, Class<T> clz) {
        return clz.cast(instance.get(key));
    }

    public static String prop(String key, String def) {
        return instance.getProperty(key, def);
    }
}
