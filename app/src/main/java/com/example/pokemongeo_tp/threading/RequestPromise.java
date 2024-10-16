package com.example.pokemongeo_tp.threading;

import java.util.UUID;
import java.util.function.Function;

public class RequestPromise<P, R> {
    private final UUID id;
    private R data;
    private final P params;
    private boolean isResolved;
    private final ThreadEventListener<R> listener;
    private Function<P, R> callback;

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
