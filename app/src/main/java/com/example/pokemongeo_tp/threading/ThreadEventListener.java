package com.example.pokemongeo_tp.threading;

/**
 * Generic Listener pour les threads.
 * @param <T> type de données à envoyer
 */
public interface ThreadEventListener<T> {
    public void OnEventInThread(T data);
}
