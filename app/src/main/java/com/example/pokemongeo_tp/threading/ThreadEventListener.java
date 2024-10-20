package com.example.pokemongeo_tp.threading;

/**
 * Generic Listener pour les threads.
 *
 * @param <T> type de données retourné par le thread
 */
public interface ThreadEventListener<T> {
    void OnEventInThread(T data);

    void OnEventInThreadReject(String error);
}
