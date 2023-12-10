package sb.bookshelf.app.services;

import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sb.bookshelf.app.ui.BookPanel;
import sb.bookshelf.common.messages.TotalBooks;

@Slf4j
@RequiredArgsConstructor
public final class CountVolumes extends ExecService {

    private final BookPanel books;

    private void countCallback(HttpResponse<TotalBooks> response) {
        if (response.isSuccess()) {
            books.count(response.getBody().getCount());
        } else {
            LOG.warn("{} {}", this.getClass(), response.getStatus());
        }
    }


    @Override
    public void run() {
        Unirest.get("/api/count/")
                .asObjectAsync(TotalBooks.class, this::countCallback);
    }
}
