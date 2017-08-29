package com.example.rifar.belanegara.module;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by asus on 8/24/2017.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BehaviorSelector {
    Class<? extends View> viewType() default View.class;
    String value();
}
