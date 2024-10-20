package com.example.pokemongeo_tp.threading;

import android.util.Log;

import java.util.UUID;
import java.util.function.Function;

public class RequestPromise<P, R> {
    private final UUID id;
    private final ThreadEventListener<R> listener;
    private final Function<P, R> callback;
    private final P params;

    private R data;
    private boolean isResolved;


    public RequestPromise(ThreadEventListener<R> listener, Function<P, R> callback, P params) {
        this.id = UUID.randomUUID();
        this.listener = listener;
        this.callback = callback;
        this.params = params;
    }

    public void resolve() {
        data = callback.apply(params);
        isResolved = true;
        listener.OnEventInThread(data);
    }

    public void reject(String error) {
        isResolved = false;
        Log.i("ERROR", "Rejecting request on listener");
        listener.OnEventInThreadReject(error);
    }

    public boolean isResolved() {
        return isResolved;
    }

    public UUID getId() {
        return id;
    }

    public R getData() {
        return data;
    }

}
