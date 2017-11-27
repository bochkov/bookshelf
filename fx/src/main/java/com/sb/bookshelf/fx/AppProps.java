package com.sb.bookshelf.fx;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public final class AppProps {

    public static final String HOST = "bookshelf.host";
    public static final String PORT = "bookshelf.port";
    public static final String USER = "bookshelf.user";
    public static final String PASS = "bookshelf.pass";

    private static final File CONFIG = new File(System.getProperty("user.home"), ".bookshelf.properties");

    private final Properties props = new Properties();
    private final StringProperty hostProperty = new SimpleStringProperty();
    private final IntegerProperty portProperty = new SimpleIntegerProperty();
    private final StringProperty userProperty = new SimpleStringProperty();
    private final StringProperty passProperty = new SimpleStringProperty();

    public void load() {
        if (CONFIG.exists()) {
            try (FileInputStream in = new FileInputStream(CONFIG)) {
                props.load(in);
            } catch (IOException ex) {
                //
            }
        }
        hostProperty.setValue(props.getProperty(HOST, ""));
        portProperty.setValue(Integer.parseInt(props.getProperty(PORT, "80")));
        userProperty.setValue(props.getProperty(USER, ""));
        passProperty.setValue(props.getProperty(PASS, ""));
    }

    public void save() {
        props.setProperty(HOST, hostProperty.get());
        props.setProperty(PORT, portProperty.getValue().toString());
        props.setProperty(USER, userProperty.get());
        props.setProperty(PASS, passProperty.get());
        try (FileOutputStream out = new FileOutputStream(CONFIG)) {
            props.store(out, "Bookshelf properties");
        }
        catch (IOException ex) {
            //
        }
    }

    public StringProperty hostProperty() {
        return hostProperty;
    }

    public IntegerProperty portProperty() {
        return portProperty;
    }

    public StringProperty userProperty() {
        return userProperty;
    }

    public StringProperty passProperty() {
        return passProperty;
    }
}
