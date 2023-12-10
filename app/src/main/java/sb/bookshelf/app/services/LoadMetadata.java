package sb.bookshelf.app.services;

import java.util.List;

import kong.unirest.core.GenericType;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sb.bookshelf.app.ui.AuthorListener;
import sb.bookshelf.app.ui.PublisherListener;

@Slf4j
@RequiredArgsConstructor
public final class LoadMetadata extends ExecService {

    private static final GenericType<List<String>> LIST_STRING_TYPE = new GenericType<>() {
    };

    private final AuthorListener authors;
    private final PublisherListener publishers;

    private void authorsCallback(HttpResponse<List<String>> response) {
        if (response.isSuccess()) {
            authors.updateAuthors(response.getBody());
        } else {
            LOG.warn("{} {}", this.getClass(), response.getStatus());
        }
    }

    private void publishersCallback(HttpResponse<List<String>> response) {
        if (response.isSuccess()) {
            publishers.updatePublishers(response.getBody());
        } else {
            LOG.warn("{} {}", this.getClass(), response.getStatus());
        }
    }

    @Override
    public void run() {
        Unirest.get("/api/authors/")
                .asObjectAsync(LIST_STRING_TYPE, this::authorsCallback);
        Unirest.get("/api/publishers/")
                .asObjectAsync(LIST_STRING_TYPE, this::publishersCallback);
    }
}
