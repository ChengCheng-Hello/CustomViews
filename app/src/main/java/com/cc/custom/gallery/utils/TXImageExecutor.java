package com.cc.custom.gallery.utils;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * Created by Cheng on 17/3/24.
 */

public class TXImageExecutor {

    private static final String TAG = "TXImageExecutor";

    private ExecutorService mExecutorService;

    private TXImageExecutor() {
    }

    private static final class InnerHolder {
        private static final TXImageExecutor INSTANCE = new TXImageExecutor();
    }

    public static TXImageExecutor getInstance() {
        return InnerHolder.INSTANCE;
    }

    @Nullable
    public FutureTask<Boolean> runWorker(@NonNull Callable<Boolean> callable) {
        ensureWorkerHandler();
        FutureTask<Boolean> task = null;
        try {
            task = new FutureTask<>(callable);
            mExecutorService.submit(task);
            return task;
        } catch (Exception e) {
            Log.d(TAG, "callable stop running unexpected. " + e.getMessage());
        }
        return task;
    }

    public void runWorker(@NonNull Runnable runnable) {
        ensureWorkerHandler();
        mExecutorService.execute(runnable);
    }

    public void runUI(@NonNull Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
            return;
        }

        Handler handler = ensureUIHandler();
        handler.post(runnable);
    }

    private void ensureWorkerHandler() {
        if (mExecutorService == null) {
            mExecutorService = Executors.newCachedThreadPool();
        }
    }

    @NonNull
    private Handler ensureUIHandler() {
        return new Handler(Looper.getMainLooper());
    }
}
