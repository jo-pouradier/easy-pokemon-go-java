package com.example.pokemongeo_tp.threading;

/**
 * Generic Listener pour les threads.
 * @param <T> type de données retourné par le thread
 */
public interface ThreadEventListener<T> {
    public void OnEventInThread(T data);
    public void OnEventInThreadReject(String error);
}
