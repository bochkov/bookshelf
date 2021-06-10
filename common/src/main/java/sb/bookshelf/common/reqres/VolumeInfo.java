package sb.bookshelf.common.reqres;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sb.bookshelf.common.model.Volume;

@Slf4j
@Data
@NoArgsConstructor
public final class VolumeInfo implements Serializable {

    private String id;
    private String title;
    private String author;
    private String publisher;
    private String year;
    private String annotation;
    private String isbn;
    private Integer pages;
    private List<String> books;

    public Volume toVolume() {
        var volume = new Volume();
        volume.setId(id);
        volume.setTitle(title);
        volume.setAuthor(author);
        volume.setPublisher(publisher);
        volume.setYear(year);
        volume.setAnnotation(annotation);
        volume.setIsbn(isbn);
        volume.setPages(pages);
        volume.setBooks(books);
        return volume;
    }
}
