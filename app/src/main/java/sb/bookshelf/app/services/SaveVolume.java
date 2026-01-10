package sb.bookshelf.app.services;

import kong.unirest.core.Callback;
import kong.unirest.core.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sb.bookshelf.common.model.VolumeInfo;

@Slf4j
@RequiredArgsConstructor
public final class SaveVolume extends ExecService {

    private final VolumeInfo volume;
    private final Callback<VolumeInfo> callback;

    @Override
    public void run() {
        Unirest.post("/api/save/")
                .body(volume)
                .asObjectAsync(VolumeInfo.class, resp -> {
                    if (resp.isSuccess()) {
                        callback.completed(resp);
                    } else {
                        LOG.warn("{} {}", this.getClass(), resp.getStatus());
                    }
                });
    }
}
