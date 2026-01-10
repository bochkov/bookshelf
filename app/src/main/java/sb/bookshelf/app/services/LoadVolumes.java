package sb.bookshelf.app.services;

import kong.unirest.core.GenericType;
import kong.unirest.core.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sb.bookshelf.app.ui.BookPanel;
import sb.bookshelf.common.model.VolumeInfo;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public final class LoadVolumes extends ExecService {

    private static final GenericType<List<VolumeInfo>> LIST_VOLUME_TYPE = new GenericType<>() {
    };

    private final int count;
    private final BookPanel books;

    @Override
    public void run() {
        Unirest.get("/api/latest/")
                .queryString("c", String.valueOf(count))
                .asObjectAsync(LIST_VOLUME_TYPE, resp -> {
                    if (resp.isSuccess()) {
                        books.fill(resp.getBody());
                    } else {
                        LOG.warn("{} {}", this.getClass(), resp.getStatus());
                    }
                });
    }
}
