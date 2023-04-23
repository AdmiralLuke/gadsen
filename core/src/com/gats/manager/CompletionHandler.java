package com.gats.manager;

public interface CompletionHandler<T> {
    void onComplete(T instance);
}
