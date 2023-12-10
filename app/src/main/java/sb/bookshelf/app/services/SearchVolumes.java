package sb.bookshelf.app.services;

import java.util.List;

import kong.unirest.core.Callback;
import kong.unirest.core.GenericType;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sb.bookshelf.common.messages.SearchQuery;
import sb.bookshelf.common.model.Volume;

@Slf4j
@RequiredArgsConstructor
public final class SearchVolumes extends ExecService {

    private static final GenericType<List<Volume>> LIST_VOLUME_TYPE = new GenericType<>() {
    };

    private final String query;
    private final Callback<List<Volume>> callback;

    private void searchCallback(HttpResponse<List<Volume>> response) {
        if (!response.isSuccess()) {
            LOG.warn("{} {}", this.getClass(), response.getStatus());
        }
        callback.completed(response);
    }

    @Override
    public void run() {
        SearchQuery req = new SearchQuery(query);
        Unirest.post("/api/search/")
                .body(req)
                .asObjectAsync(LIST_VOLUME_TYPE, this::searchCallback);
    }
}
