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
        System.out.println("RequestThread started");
        while(true){
            RequestPromise request = null;
            try {
                request = queue.take();
                System.out.println("RequestThread got a request");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                System.out.println("RequestThread resolving a request");
                request.resolve();
                System.out.println("RequestThread resolved a request");
            } catch (Exception e) {
                System.out.println("Rejecting request");
                request.reject("THREAD ERROR" + e.toString());
                e.printStackTrace();
            }
        }
    }

}
