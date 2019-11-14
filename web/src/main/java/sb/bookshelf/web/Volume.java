package sb.bookshelf.web;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(of = "id")
@ToString(exclude = "books")
@Document(collection = Volume.COLLECTION_NAME)
public final class Volume implements Serializable {

    public static final String COLLECTION_NAME = "volumes";

    @Id
    private String id;
    private String name;
    private String author;
    private String publisher;
    private String year;
    private String annotation;
    private String isbn;
    private Integer pages;
    private List<String> books;

}
