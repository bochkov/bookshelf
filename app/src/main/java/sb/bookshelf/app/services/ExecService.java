package sb.bookshelf.app.services;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class ExecService implements Runnable {

    private static final ExecutorService SERVICE = Executors.newFixedThreadPool(5);

    public void start() {
        SERVICE.submit(this);
    }
}
