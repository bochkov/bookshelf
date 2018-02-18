package com.sb.bookshelf.fx;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
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
        lisbn.setText(
                new NotNull(
                        isbn,
                        data -> String.join("\n", isbn.split(",\\s+"))
                ).value()
        );
        lpages.setText(
                new NotNull(
                        pages,
                        data -> String.format("%s страниц", pages)
                ).value()
        );
        lbooks.setText(
                new NotNull(
                        books,
                        data -> String.join("\n", books)
                ).value()
        );
        //lbooks.setVisible(books != null && !String.join("", books).isEmpty());
        lannot.setText(annotation);
        lannot.setVisible(annotation != null && !annotation.isEmpty());
    }

    interface Func {
        String apply(Object data);
    }

    private final class NotNull {

        private final Object data;
        private final Func func;

        public NotNull(Object data, Func func) {
            this.data = data;
            this.func = func;
        }

        public String value() {
            return data != null ?
                    func.apply(data)
                    : "";
        }
    }
}