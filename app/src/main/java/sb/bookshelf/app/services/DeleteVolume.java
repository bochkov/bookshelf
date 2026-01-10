package sb.bookshelf.app.services;

import kong.unirest.core.Callback;
import kong.unirest.core.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sb.bookshelf.common.messages.DeleteResponse;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public final class DeleteVolume extends ExecService {

    private final List<String> volumesIds;
    private final Callback<DeleteResponse> callback;

    @Override
    public void run() {
        Unirest.post("/api/delete/")
                .body(volumesIds)
                .asObjectAsync(DeleteResponse.class, resp -> {
                    if (resp.isSuccess()) {
                        callback.completed(resp);
                    } else {
                        LOG.warn("{} {}", this.getClass(), resp.getStatus());
                    }
                });
    }
}
