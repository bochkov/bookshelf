package com.sb.bookshelf.fx;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import lombok.Data;

import java.util.List;

@Data
public final class Volume {

    private String id;
    private String name;
    private String author;
    private String publisher;
    private String year;
    private String annotation;
    private String isbn;
    private Integer pages;
    private List<String> books;

    public String title() {
        return (author != null && !author.isEmpty()) ?
                String.format("%s%s %s",
                        author,
                        author.endsWith(".") ?
                                "" :
                                ".",
                        name
                ) :
                name;
    }

    public void fill(Label lauthor, Label lname, Label lpublish,
                     Label lyear, Label lisbn, Label lpages,
                     TextArea lbooks, TextArea lannot) {
        lauthor.setText(author);
        lname.setText(name);
        lpublish.setText(publisher);
        lyear.setText(year);
        lisbn.setText(isbn);
        lpages.setText(pages == null ?
                "" :
                String.format("%s страниц", pages)
        );

        lbooks.setText(books == null ? "" : String.join("\n", books));
        //lbooks.setVisible(books != null && !String.join("", books).isEmpty());

        lannot.setText(annotation);
        lannot.setVisible(annotation != null && !annotation.isEmpty());
    }
}