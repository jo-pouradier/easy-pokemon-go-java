package com.example.pokemongeo_tp.threading;

import android.util.Log;

import java.util.concurrent.LinkedBlockingQueue;

public class RequestThread extends java.lang.Thread {
    private static final int MAX_QUEUE_SIZE = 100;
    private static RequestThread instance;
    private static boolean isRunning = false;
    private final LinkedBlockingQueue<RequestPromise<?,?>> queue = new LinkedBlockingQueue<>(MAX_QUEUE_SIZE);

    private RequestThread() {
    }

    public static RequestThread getInstance() {
        if (instance == null) {
            instance = new RequestThread();
        }
        return instance;
    }

    public void addRequest(RequestPromise<?,?> request) {
        queue.add(request);
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void run() {
        isRunning = true;
        while (isRunning) {
            RequestPromise<?,?> request;

            try {
                request = queue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            try {
                request.resolve();
            } catch (Exception e) {
                request.reject("THREAD ERROR " + e);
                Log.e("ERROR", "Error while resolving request", e);
            }
        }
    }

}
