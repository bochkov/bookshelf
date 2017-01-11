package com.sergeybochkov.bookshelf.fx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public final class AppProperties extends Properties {

    public static final String HOST = "host";
    public static final String PORT = "port";

    private static final File CONFIG_DIR = new File(System.getProperty("user.home"), ".bookshelf");
    private static final File CONFIG_FILE = new File(CONFIG_DIR, "bookshelf.properties");

    public void load() {
        if (CONFIG_DIR.exists() || CONFIG_DIR.mkdirs()) {
            try (FileInputStream in = new FileInputStream(CONFIG_FILE)) {
                load(in);
            } catch (IOException ex) {
                //
            }
        }
        else
            System.out.println("Ошибка создания каталога свойств " + CONFIG_DIR.getAbsolutePath());
    }

    public void save() {
        try (FileOutputStream out = new FileOutputStream(CONFIG_FILE)) {
            store(out, "Bookshelf properties");
        }
        catch (IOException ex) {
            //
        }
    }

    public String host() {
        return getProperty(HOST);
    }

    public void setHost(String host) {
        setProperty(HOST, host);
    }

    public int port() {
        return Integer.parseInt(getProperty(PORT, "0"));
    }

    public void setPort(int port) {
        setProperty(PORT, String.valueOf(port));
    }
}
