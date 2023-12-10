package sb.bookshelf.app.services;

import java.util.List;

import kong.unirest.core.GenericType;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sb.bookshelf.app.ui.BookPanel;
import sb.bookshelf.common.model.VolumeInfo;

@Slf4j
@RequiredArgsConstructor
public final class LoadVolumes extends ExecService {

    private static final GenericType<List<VolumeInfo>> LIST_VOLUME_TYPE = new GenericType<>() {
    };

    private final int count;
    private final BookPanel books;

    private void volumesCallback(HttpResponse<List<VolumeInfo>> response) {
        if (response.isSuccess()) {
            books.fill(response.getBody());
        } else {
            LOG.warn("{} {}", this.getClass(), response.getStatus());
        }
    }

    @Override
    public void run() {
        Unirest.get("/api/latest/")
                .queryString("c", String.valueOf(count))
                .asObjectAsync(LIST_VOLUME_TYPE, this::volumesCallback);
    }
}
