package sb.bookshelf.app.services;

import kong.unirest.core.Callback;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sb.bookshelf.common.messages.DeleteRequest;
import sb.bookshelf.common.messages.DeleteResponse;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class DeleteVolume extends ExecService {

    private final List<String> volumesIds;
    private final Callback<DeleteResponse> callback;

    private void deleteCallback(HttpResponse<DeleteResponse> response) {
        if (!response.isSuccess()) {
            LOG.warn("{} {}", this.getClass(), response.getStatus());
        }
        callback.completed(response);
    }

    @Override
    public void run() {
        DeleteRequest req = new DeleteRequest(volumesIds);
        Unirest.post("/api/delete/")
                .body(req)
                .asObjectAsync(DeleteResponse.class, this::deleteCallback);
    }
}
