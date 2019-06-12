module bookshelf {

    requires jdk.localedata;
    requires java.desktop;

    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires jcabi.http;
    requires java.json;

    requires static lombok;

    opens com.sb.bookshelf.fx to javafx.fxml;

    exports com.sb.bookshelf.fx;
}