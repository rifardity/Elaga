package com.example.rifar.belanegara.module;

import android.view.View;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by asus on 8/24/2017.
 */

public abstract class Behavior {
    void applyBehavior(View view, String selector) throws InvocationTargetException, IllegalAccessException {
        Class viewType = view.getClass();
        for(Method method : this.getClass().getDeclaredMethods()) {
            Class listenerType = method.getReturnType();
            BehaviorSelector behaviorParams = method.getAnnotation(BehaviorSelector.class);

            if(behaviorParams != null && !behaviorParams.value().equals(selector))
                continue;

            if(listenerType.isInterface()) {
                if(listenerType.getDeclaringClass().isAssignableFrom(viewType) ||
                        (behaviorParams != null && behaviorParams.viewType() == viewType)) {
                    Method listener = BindingContext.hasListenerCached(viewType, listenerType);
                    if(listener == null)
                        listener = mapListener(view.getClass(), listenerType);

                    listener.invoke(view, method.invoke(this));
                }
            }
        }
    }

    private Method mapListener(Class viewType, Class listenerType) {
        Method listener = BindingContext.hasListenerCached(viewType, listenerType);
        if(listener == null) {
            try {
                listener = viewType.getMethod("set" + listenerType.getSimpleName(), listenerType);
            } catch (NoSuchMethodException e) {
                for(Method m : viewType.getMethods()) {
                    if(m.getParameterTypes().length > 0 && m.getParameterTypes()[0] == listenerType) {
                        listener = m;
                        break;
                    }
                }
            }
        }

        BindingContext.registerCache(viewType, listenerType, listener);
        return listener;
    }
}
