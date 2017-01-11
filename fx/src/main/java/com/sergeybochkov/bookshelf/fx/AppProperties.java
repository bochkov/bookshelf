package com.sergeybochkov.bookshelf.fx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class AppProperties extends Properties {

    public static final String HOST = "host";
    public static final String PORT = "port";

    private File dir = new File(System.getProperty("user.home") + File.separator + ".bookshelf");
    private File config = new File(dir, "bookshelf.properties");

    public void load() {
        if (!dir.exists() && !dir.mkdirs()) {
            System.out.println("Ошибка создания каталога свойств " + dir.getAbsolutePath());
            return;
        }
        try (FileInputStream in = new FileInputStream(config)) {
            load(in);
        }
        catch (IOException ex) {
            //
        }
    }

    public void save() {
        try (FileOutputStream out = new FileOutputStream(config)) {
            store(out, "Bookshelf properties");
        }
        catch (IOException ex) {
            //
        }
    }

    public String getHost() {
        return getProperty(HOST);
    }

    public void setHost(String host) {
        setProperty(HOST, host);
    }

    public int getPort() {
        return Integer.parseInt(getProperty(PORT, "0"));
    }

    public void setPort(int port) {
        setProperty(PORT, String.valueOf(port));
    }
}
