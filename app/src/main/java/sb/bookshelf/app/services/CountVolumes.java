package sb.bookshelf.app.services;

import kong.unirest.core.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sb.bookshelf.app.ui.BookPanel;
import sb.bookshelf.common.messages.TotalVolumes;

@Slf4j
@RequiredArgsConstructor
public final class CountVolumes extends ExecService {

    private final BookPanel books;

    @Override
    public void run() {
        Unirest.get("/api/count/")
                .asObjectAsync(TotalVolumes.class, resp -> {
                    if (resp.isSuccess()) {
                        books.total(resp.getBody().getCount());
                    } else {
                        LOG.warn("{} {}", this.getClass(), resp.getStatus());
                    }
                });
    }
}
