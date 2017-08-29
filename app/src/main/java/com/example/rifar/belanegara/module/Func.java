package com.example.rifar.belanegara.module;

/**
 * Created by asus on 8/15/2017.
 */

public interface Func<TOut, TIn> {
    TOut invoke(TIn obj);
}
