package sb.bookshelf.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(of = "id")
@ToString(exclude = "books")
public final class VolumeInfo {

    private String id;
    private String title;
    private String author;
    private String publisher;
    private String year;
    private String annotation;
    private String isbn;
    private Integer pages;
    private List<String> books;

    public String printBooks() {
        return isBooksPresent() ?
                String.join("\n", books) : "";
    }

    public boolean isBooksPresent() {
        return books != null && !books.isEmpty();
    }

    public String desc() {
        String delimiter = author == null || author.endsWith(".") ? " " : ". ";
        List<String> data = new ArrayList<>();
        if (author != null && !author.isEmpty()) {
            data.add(author);
        }
        if (title != null && !title.isEmpty()) {
            data.add(title);
        }
        return String.join(delimiter, data);
    }

    public String metaData() {
        List<String> data = new ArrayList<>();
        if (publisher != null && !publisher.isEmpty()) {
            data.add(publisher);
        }
        if (year != null && !year.isEmpty()) {
            data.add(year);
        }
        if (isbn != null && !isbn.isEmpty()) {
            data.add("ISBN " + isbn);
        }
        if (pages != null) {
            data.add(pages + " стр.");
        }
        return String.join(", ", data);
    }

}
