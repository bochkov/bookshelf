package sb.bookshelf.app.services;

import kong.unirest.core.Callback;
import kong.unirest.core.GenericType;
import kong.unirest.core.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sb.bookshelf.common.messages.SearchQuery;
import sb.bookshelf.common.model.VolumeInfo;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public final class SearchVolumes extends ExecService {

    private static final GenericType<List<VolumeInfo>> LIST_VOLUME_TYPE = new GenericType<>() {
    };

    private final String query;
    private final Callback<List<VolumeInfo>> callback;

    @Override
    public void run() {
        Unirest.post("/api/search/")
                .body(new SearchQuery(query))
                .asObjectAsync(LIST_VOLUME_TYPE, resp -> {
                    if (resp.isSuccess()) {
                        callback.completed(resp);
                    } else {
                        LOG.warn("{} {}", this.getClass(), resp.getStatus());
                    }
                });
    }
}
