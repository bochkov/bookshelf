package sb.bookshelf.app.services;

import java.util.List;

import kong.unirest.core.Callback;
import kong.unirest.core.Unirest;
import lombok.RequiredArgsConstructor;
import sb.bookshelf.common.reqres.DelInfo;

@RequiredArgsConstructor
public class DeleteVolume extends ExecService {

    private final List<String> volumes;
    private final Callback<DelInfo> callback;

    @Override
    public void run() {
        var info = new DelInfo();
        info.setIds(volumes);
        Unirest.post("/api/delete/")
                .body(info)
                .asObjectAsync(DelInfo.class, new LoggedCallback<>(callback));
    }
}
