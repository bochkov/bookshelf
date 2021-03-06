package sb.bookshelf.app.services;

import java.util.List;

import kong.unirest.GenericType;
import kong.unirest.Unirest;
import lombok.RequiredArgsConstructor;
import sb.bookshelf.app.ui.AuthorListener;
import sb.bookshelf.app.ui.PublisherListener;

@RequiredArgsConstructor
public final class LoadMetadata extends ExecService {

    private final AuthorListener authors;
    private final PublisherListener publishers;

    @Override
    public void run() {
        Unirest.get("/api/authors/")
                .header("Accept", "application/json")
                .asObjectAsync(
                        new GenericType<List<String>>() {
                        },
                        new LoggedCallback<>(
                                resp -> {
                                    if (resp.isSuccess())
                                        authors.authors(resp.getBody());
                                }
                        )
                );
        Unirest.get("/api/publishers/")
                .header("Accept", "application/json")
                .asObjectAsync(
                        new GenericType<List<String>>() {
                        },
                        new LoggedCallback<>(
                                resp -> {
                                    if (resp.isSuccess())
                                        publishers.publishers(resp.getBody());
                                }
                        )
                );
    }
}
