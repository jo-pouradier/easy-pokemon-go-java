package com.example.pokemongeo_tp.threading;

import java.util.concurrent.ArrayBlockingQueue;

public class RequestThread extends java.lang.Thread {
    private final ArrayBlockingQueue<RequestPromise> queue = new ArrayBlockingQueue<>(10);
    private static RequestThread instance;

    private RequestThread() {
    }

    public static RequestThread getInstance() {
        if (instance == null) {
            instance = new RequestThread();
        }
        return instance;
    }

    public void addRequest(RequestPromise request) {
        queue.add(request);
    }

    public void run() {
        while (true) {
            RequestPromise request = null;
            try {
                request = queue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                request.resolve();
            } catch (Exception e) {
                request.reject("THREAD ERROR" + e.toString());
                e.printStackTrace();
            }
        }
    }

}
