package rodriguezmunoz.jonathan.comexamplenewsapp.data.repository;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {
    private static final int THREAD_COUNT = 3;
    private static AppExecutors instance;

    private final Executor diskIO = Executors.newSingleThreadExecutor();
    private final Executor networkIO = Executors.newFixedThreadPool(THREAD_COUNT);
    private final Executor mainThread = new MainThreadExecutor();

    public static AppExecutors getInstance() {
        if (instance == null) instance = new AppExecutors();
        return instance;
    }

    public Executor diskIO()     { return diskIO; }
    public Executor networkIO()  { return networkIO; }
    public Executor mainThread() { return mainThread; }

    private static class MainThreadExecutor implements Executor {
        private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());
        @Override
        public void execute(Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}