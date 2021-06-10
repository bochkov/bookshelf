package sb.bookshelf.app.services;

import java.util.List;

import kong.unirest.Callback;
import kong.unirest.GenericType;
import kong.unirest.Unirest;
import lombok.RequiredArgsConstructor;
import sb.bookshelf.common.model.Volume;
import sb.bookshelf.common.reqres.SearchQuery;

@RequiredArgsConstructor
public final class SearchVolumes extends ExecService {

    private final String query;
    private final Callback<List<Volume>> callback;

    @Override
    public void run() {
        Unirest.post("/api/search/")
                .header("Content-Type", "application/json")
                .body(new SearchQuery(query))
                .asObjectAsync(
                        new GenericType<>() {
                        },
                        new LoggedCallback<>(callback)
                );
    }
}
