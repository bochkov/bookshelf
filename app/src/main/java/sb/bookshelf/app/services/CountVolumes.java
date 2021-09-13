package sb.bookshelf.app.services;

import kong.unirest.GenericType;
import kong.unirest.Unirest;
import lombok.RequiredArgsConstructor;
import sb.bookshelf.app.ui.BookPanel;
import sb.bookshelf.common.reqres.TotalBooks;

@RequiredArgsConstructor
public final class CountVolumes extends ExecService {

    private final BookPanel books;

    @Override
    public void run() {
        Unirest.get("/api/count/")
                .asObjectAsync(
                        new GenericType<TotalBooks>() {
                        },
                        new LoggedCallback<>(
                                resp -> {
                                    if (resp.isSuccess())
                                        books.count(resp.getBody().getCount());
                                }
                        )
                );
    }
}
