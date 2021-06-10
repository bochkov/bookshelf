package sb.bookshelf.app.services;

import java.util.List;

import kong.unirest.GenericType;
import kong.unirest.Unirest;
import lombok.RequiredArgsConstructor;
import sb.bookshelf.app.ui.BookPanel;
import sb.bookshelf.common.model.Volume;

@RequiredArgsConstructor
public final class LoadVolumes extends ExecService {

    private final BookPanel books;

    @Override
    public void run() {
        Unirest.get("/api/list/")
                .header("Accept", "application/json")
                .asObjectAsync(
                        new GenericType<List<Volume>>() {
                        },
                        new LoggedCallback<>(
                                response -> {
                                    if (response.isSuccess())
                                        books.fill(response.getBody());
                                }
                        )
                );
    }
}
