package sb.bookshelf.app.services;

import kong.unirest.Callback;
import kong.unirest.HttpResponse;
import kong.unirest.UnirestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public final class LoggedCallback<T> implements Callback<T> {

    private final Callback<T> origin;

    @Override
    public void completed(HttpResponse<T> response) {
        LOG.debug("{} = {}", origin.getClass().getSimpleName(), response.getStatus());
        origin.completed(response);
    }

    @Override
    public void failed(UnirestException e) {
        LOG.debug(e.getMessage(), e);
        origin.failed(e);
    }

    @Override
    public void cancelled() {
        LOG.debug("cancelled");
        origin.cancelled();
    }
}
