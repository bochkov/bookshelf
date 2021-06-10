package sb.bookshelf.app.services;

import kong.unirest.Callback;
import kong.unirest.Unirest;
import lombok.RequiredArgsConstructor;
import sb.bookshelf.common.model.Volume;

@RequiredArgsConstructor
public final class SaveVolume extends ExecService {

    private final Volume volume;
    private final Callback<Volume> callback;

    @Override
    public void run() {
        Unirest.post("/api/save/")
                .header("Content-Type", "application/json")
                .body(volume)
                .asObjectAsync(Volume.class, new LoggedCallback<>(callback));
    }
}
