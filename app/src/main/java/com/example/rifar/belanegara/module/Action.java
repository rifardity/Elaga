package com.example.rifar.belanegara.module;

/**
 * Created by asus on 8/15/2017.
 */

public interface Action<T> {
    void invoke(T obj);
}
