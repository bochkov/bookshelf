package sb.bookshelf.app.services;

import kong.unirest.core.GenericType;
import kong.unirest.core.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sb.bookshelf.app.ui.AuthorListener;
import sb.bookshelf.app.ui.PublisherListener;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public final class LoadMetadata extends ExecService {

    private static final GenericType<List<String>> LIST_STRING_TYPE = new GenericType<>() {
    };

    private final AuthorListener authors;
    private final PublisherListener publishers;

    @Override
    public void run() {
        Unirest.get("/api/authors/")
                .asObjectAsync(LIST_STRING_TYPE, resp -> {
                    if (resp.isSuccess()) {
                        authors.updateAuthors(resp.getBody());
                    } else {
                        LOG.warn("{} {}", this.getClass(), resp.getStatus());
                    }
                });
        Unirest.get("/api/publishers/")
                .asObjectAsync(LIST_STRING_TYPE, resp -> {
                    if (resp.isSuccess()) {
                        publishers.updatePublishers(resp.getBody());
                    } else {
                        LOG.warn("{} {}", this.getClass(), resp.getStatus());
                    }
                });
    }
}
