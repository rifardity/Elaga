package com.example.rifar.belanegara.module;

import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by asus on 8/17/2017.
 */

public class BindingContext {
    private static HashMap<Class, ArrayList<ListenerBindingContext>> mListenerCache = new HashMap<>();
    private static ArrayList<ViewBindingContext> mBindingCache = new ArrayList<>();
    private static ArrayList<LayoutBindingContext> mLayoutCache = new ArrayList<>();
    private static HashMap<Class, Method> mSourcesCache = new HashMap<>();
    private static HashMap<Class<? extends Behavior>, Behavior> mBehaviorCache = new HashMap<>();

    static {
        try {
            registerDefault();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private BindingContext() { }

    static BindingExpression hasCached(Class memberType, Class viewType, String viewAttr) {
        for(ViewBindingContext context : mBindingCache) {
            if(context.viewType.isAssignableFrom(viewType)) {
                for(BindingExpression expr : context.bindingCache) {
                    if(expr.equals(memberType, viewType, viewAttr))
                        return expr;
                }
            }
        }
        return null;
    }

    static boolean hasCached(@LayoutRes int layout, Class dataType) {
        for(LayoutBindingContext context : mLayoutCache) {
            if(context.dataType == dataType && context.layout == layout)
                return true;
        }
        return false;
    }

    static Method hasAdapterCached(Class viewType) {
        if(mSourcesCache.containsKey(viewType))
            return mSourcesCache.get(viewType);
        return null;
    }

    static Method hasListenerCached(Class viewType, Class listenerType) {
        if(mListenerCache.containsKey(viewType)) {
            for(ListenerBindingContext context : mListenerCache.get(viewType)) {
                if(context.listenerType == listenerType)
                    return context.listener;
            }
        }
        return null;
    }

    static Behavior hasBehaviorCached(Class<? extends Behavior> cls) {
        if(mBehaviorCache.containsKey(cls))
            return mBehaviorCache.get(cls);
        return null;
    }

    static void registerBehavior(Class<? extends Behavior> cls, Behavior obj) {
        mBehaviorCache.put(cls, obj);
    }

    static void registerCache(Class viewType, BindingExpression expr) {
        for(ViewBindingContext context : mBindingCache) {
            if(context.viewType == viewType) {
                context.bindingCache.add(expr);
                return;
            }
        }
        ViewBindingContext newContext = new ViewBindingContext();
        newContext.viewType = viewType;
        newContext.bindingCache.add(expr);
        mBindingCache.add(newContext);
    }

    static void registerCache(@LayoutRes int layout, Class dataType, Field field, Class type, String attrName, Integer[] depths, BindingMode mode) {
        LayoutBindingSubscriber subscriber = new LayoutBindingSubscriber();
        subscriber.attrName = attrName;
        subscriber.field = field;
        subscriber.type = type;
        subscriber.depths = depths;
        subscriber.mode = mode;
        for(LayoutBindingContext context : mLayoutCache) {
            if(context.dataType == dataType && context.layout == layout) {
                context.subscribers.add(subscriber);
                return;
            }
        }
        LayoutBindingContext context = new LayoutBindingContext();
        context.dataType = dataType;
        context.layout = layout;
        mLayoutCache.add(context);
        context.subscribers.add(subscriber);
    }

    static void registerCache(@LayoutRes int layout, Class dataType, String behaviorName, Integer[] depths, String selector) {
        ListenerBindingSubscriber listener = new ListenerBindingSubscriber();
        listener.name = behaviorName;
        listener.depths = depths;
        listener.selector = selector;
        for(LayoutBindingContext context : mLayoutCache) {
            if(context.dataType == dataType && context.layout == layout) {
                context.listeners.add(listener);
                return;
            }
        }
        LayoutBindingContext context = new LayoutBindingContext();
        context.dataType = dataType;
        context.layout = layout;
        mLayoutCache.add(context);
        context.listeners.add(listener);
    }

    static void registerCache(@LayoutRes int layout, Class dataType, @LayoutRes int adapterLayout, Field field, Class viewType, Method setter, Integer[] depths) {
        SourceBindingSubscriber adapter = new SourceBindingSubscriber();
        adapter.setter = setter;
        adapter.field = field;
        adapter.viewType = viewType;
        adapter.depths = depths;
        adapter.layout = adapterLayout;
        for(LayoutBindingContext context : mLayoutCache) {
            if(context.dataType == dataType && context.layout == layout) {
                context.sources.add(adapter);
                return;
            }
        }
        LayoutBindingContext context = new LayoutBindingContext();
        context.dataType = dataType;
        context.layout = layout;
        mLayoutCache.add(context);
        context.sources.add(adapter);
    }

    static void registerCache(Class viewType, Class listenerType, Method listener) {
        ArrayList<ListenerBindingContext> list ;
        if(mListenerCache.containsKey(viewType)) {
            list = mListenerCache.get(viewType);
        }
        else {
            list = new ArrayList<>();
            mListenerCache.put(viewType, list);
        }
        ListenerBindingContext context = new ListenerBindingContext();
        context.viewType = viewType;
        context.listener = listener;
        context.listenerType = listenerType;
        list.add(context);
    }

    static void registerAdapter(Class viewType, Method method) {
        mSourcesCache.put(viewType, method);
    }

    static void applyBindingCache(BindingApplicator applicator, View root, @LayoutRes int layout, Object viewModel) throws IllegalAccessException, InvocationTargetException {
        for(LayoutBindingContext context : mLayoutCache) {
            if(context.dataType == viewModel.getClass() && context.layout == layout) {
                for(LayoutBindingSubscriber subscriber : context.subscribers) {
                    BindableMember member = (BindableMember) subscriber.field.get(viewModel);
                    View view = resolveDepth(subscriber.depths, root);
                    BindingExpression expr = new BindingExpression(subscriber.type, view, subscriber.attrName, subscriber.mode);
                    member.createBinding(expr);
                    applicator.subscribeBinding(member, expr);
                }
                for(SourceBindingSubscriber source : context.sources) {
                    BindableList list = (BindableList) source.field.get(viewModel);
                    View view = resolveDepth(source.depths, root);
                    BindableAdapter a = new BindableAdapter(view.getContext(), list, source.layout);
                    source.setter.invoke(view, a);
                    list.atttachAdapter(a);
                    applicator.subscribeSource(list);
                }
                for(ListenerBindingSubscriber listener : context.listeners) {
                    View view = resolveDepth(listener.depths, root);
                    applicator.subscribeBehavior(listener.name, view, listener.selector);
                }
            }
        }
    }

    static View resolveDepth(Integer[] depths, View root) {
        View view = null;
        for(int i : depths) {
            view = view  == null? root : ((ViewGroup)view).getChildAt(i);
        }
        return view;
    }

    static void registerDefault() throws NoSuchMethodException {
        registerCache(ImageView.class, new BindingExpression(Drawable.class, ImageView.class, "src",
                ImageView.class.getMethod("getDrawable"),
                ImageView.class.getMethod("setImageDrawable", Drawable.class)));
        registerCache(AppCompatImageView.class, new BindingExpression(Drawable.class, AppCompatImageView.class, "src",
                AppCompatImageView.class.getMethod("getDrawable"),
                AppCompatImageView.class.getMethod("setImageDrawable", Drawable.class)));
        registerCache(View.class, new BindingExpression(Integer.class, View.class, "background",
                View.class.getMethod("getBackground"),
                View.class.getMethod("setBackgroundColor", int.class)));
    }

    private static class ViewBindingContext {
        Class viewType;
        ArrayList<BindingExpression> bindingCache = new ArrayList<>();
    }

    private static class LayoutBindingSubscriber {
        Field field;
        Class type;
        String attrName;
        Integer[] depths;
        BindingMode mode;
    }

    private static class LayoutBindingContext {
        @LayoutRes int layout;
        Class dataType;
        ArrayList<LayoutBindingSubscriber> subscribers = new ArrayList<>();
        ArrayList<SourceBindingSubscriber> sources = new ArrayList<>();
        ArrayList<ListenerBindingSubscriber> listeners = new ArrayList<>();
    }

    private static class SourceBindingSubscriber {
        Field field;
        Integer[] depths;
        Class viewType;
        Method setter;
        @LayoutRes int layout;
    }

    private static class ListenerBindingSubscriber {
        String name;
        Integer[] depths;
        String selector;
    }

    private static class ListenerBindingContext {
        Class viewType;
        Class listenerType;
        Method listener;
    }
}
