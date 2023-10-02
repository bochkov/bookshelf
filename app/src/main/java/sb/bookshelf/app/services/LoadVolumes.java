package sb.bookshelf.app.services;

import java.util.List;

import kong.unirest.core.GenericType;
import kong.unirest.core.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sb.bookshelf.app.ui.BookPanel;
import sb.bookshelf.common.model.Volume;

@Slf4j
@RequiredArgsConstructor
public final class LoadVolumes extends ExecService {

    private final BookPanel books;

    @Override
    public void run() {
        Unirest.get("/api/list/")
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
