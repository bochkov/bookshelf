package sb.bookshelf.app.services;

import kong.unirest.core.Callback;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sb.bookshelf.common.model.Volume;
import sb.bookshelf.common.model.VolumeInfo;

@Slf4j
@RequiredArgsConstructor
public final class SaveVolume extends ExecService {

    private final VolumeInfo volume;
    private final Callback<Volume> callback;

    private void saveCallback(HttpResponse<Volume> response) {
        if (!response.isSuccess()) {
            LOG.warn("{} {}", this.getClass(), response.getStatus());
        }
        callback.completed(response);
    }

    @Override
    public void run() {
        Unirest.post("/api/save/")
                .body(volume)
                .asObjectAsync(Volume.class, this::saveCallback);
    }
}
