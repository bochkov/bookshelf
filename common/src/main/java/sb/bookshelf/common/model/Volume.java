package sb.bookshelf.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Slf4j
@Data
@EqualsAndHashCode(of = "id")
@ToString(exclude = "books")
@Document(collection = Volume.COLLECTION_NAME)
public final class Volume implements Serializable {

    public static final String COLLECTION_NAME = "volumes";

    @Id
    private String id;
    @Indexed
    private String title;
    @Indexed
    private String author;
    private String publisher;
    private String year;
    private String annotation;
    private String isbn;
    private Integer pages;
    private List<String> books;

    public boolean isBooksPresent() {
        return books != null && !books.isEmpty();
    }

    public String desc() {
        String delimiter = author == null || author.endsWith(".") ? " " : ". ";
        List<String> data = new ArrayList<>();
        if (author != null && !author.isEmpty())
            data.add(author);
        if (title != null && !title.isEmpty())
            data.add(title);
        return String.join(delimiter, data);
    }

    public String metaData() {
        List<String> data = new ArrayList<>();
        if (publisher != null && !publisher.isEmpty())
            data.add(publisher);
        if (year != null && !year.isEmpty())
            data.add(year);
        if (isbn != null && !isbn.isEmpty())
            data.add("ISBN " + isbn);
        if (pages != null)
            data.add(pages + " стр.");
        return String.join(", ", data);
    }

}
