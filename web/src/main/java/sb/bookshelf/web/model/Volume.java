package sb.bookshelf.web.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import sb.bookshelf.common.model.VolumeInfo;

@Slf4j
@Data
@NoArgsConstructor
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
    @Indexed
    private String publisher;
    private String year;
    private String annotation;
    private String isbn;
    private Integer pages;
    private List<String> books;

    public Volume(VolumeInfo v) {
        this.id = v.getId();
        this.title = v.getTitle();
        this.author = v.getAuthor();
        this.publisher = v.getPublisher();
        this.year = v.getYear();
        this.isbn = v.getIsbn();
        this.pages = v.getPages();
        this.books = v.getBooks();
    }

    public VolumeInfo toVolumeInfo() {
        VolumeInfo vi = new VolumeInfo();
        vi.setId(id);
        vi.setTitle(title);
        vi.setAuthor(author);
        vi.setPublisher(publisher);
        vi.setYear(year);
        vi.setIsbn(isbn);
        vi.setPages(pages);
        vi.setBooks(books);
        return vi;
    }

}
