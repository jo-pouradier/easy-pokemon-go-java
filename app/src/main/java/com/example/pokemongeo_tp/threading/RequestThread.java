package com.example.pokemongeo_tp.threading;

import android.os.Bundle;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.Function;

public class RequestThread extends java.lang.Thread {
    private final ArrayBlockingQueue<RequestPromise> queue = new ArrayBlockingQueue<>(10);
    private static RequestThread instance;

    private RequestThread(){}

    public static RequestThread getInstance(){
        if (instance == null) {
            instance = new RequestThread();
        }
        return instance;
    }

    public void addRequest(RequestPromise request){
        queue.add(request);
    }

    public void run(){
        while(true){
            RequestPromise request = null;
            try {
                request = queue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                request.resolve();
            } catch (Exception e) {
                e.printStackTrace();
                request.reject(e.toString());
            }
        }
    }

}
