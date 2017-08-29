package com.example.rifar.belanegara.module;

import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by asus on 8/15/2017.
 */

public class BindingExpression {
    private Class mViewType;
    private BindingMode mMode;
    private Class mMemberType;
    private View mView;
    private String mViewAttr;
    private BindingSubscriber mSetter;
    private BindingSubscriber mGetter;
    private boolean mUsingStringConversion = false;

    public boolean equals(Class memberType, Class viewType, String viewAttr) {
        return mViewType.isAssignableFrom(viewType) && mMemberType.isAssignableFrom(memberType) && viewAttr.equals(mViewAttr);
    }

    private BindingExpression() { }

    BindingExpression(Class memberType, Class viewType, String viewAttr) {
        mMemberType = memberType;
        mViewType = viewType;
        mViewAttr = viewAttr;
    }

    BindingExpression(Class memberType, Class viewType, String viewAttr, Method getter, Method setter) {
        this(memberType, viewType, viewAttr);
        mGetter = new BindingSubscriber(getter);
        mSetter = new BindingSubscriber(setter);
    }

    BindingExpression(Class memberType, Class viewType, String viewAttr, Field getter, Field setter) {
        this(memberType, viewType, viewAttr);
        mGetter = new BindingSubscriber(getter);
        mSetter = new BindingSubscriber(setter);
    }

    public BindingExpression(Class type, View view, String memberName, BindingMode mode) {
        BindingExpression cached = BindingContext.hasCached(type, view.getClass(), memberName);

        mView = view;
        mMode = mode;
        if(cached != null) {
            copyCache(cached);
            mViewType = view.getClass();
        }
        else {
            mMemberType = type;
            mViewAttr = memberName;
            mViewType = view.getClass();
            mapGetter();
            mapSetter();
            BindingContext.registerCache(mViewType, new BindingExpression().copyCache(this));
        }

        if(mMode == BindingMode.TwoWay || mMode == BindingMode.OneWayToSource) {
            View.OnFocusChangeListener listener = view.getOnFocusChangeListener();
            if(listener != null)
                view.setOnFocusChangeListener((v, status) -> {
                    this.fetch();
                    listener.onFocusChange(v, status);
                });
            else
                view.setOnFocusChangeListener((v, status) -> this.fetch());
        }
    }

    public Object fetch() {
        if(mGetter.isValid())
            return mGetter.demand(mView);
        return null;
    }

    public void notify(Object obj) {
        if(mSetter.isValid()) {
            if(mUsingStringConversion)
                mSetter.send(mView, obj.toString());
            else
                mSetter.send(mView, obj);
        }
    }

    public BindingMode getMode() {
        return mMode;
    }

    private BindingExpression copyCache(BindingExpression expr) {
        mViewType = expr.mViewType;
        mViewAttr = expr.mViewAttr;
        mMemberType = expr.mMemberType;
        mGetter = expr.mGetter;
        mSetter = expr.mSetter;
        return this;
    }

    private void mapSetter() {
        String name = capitalize(mViewAttr);

        Method alt = null;
        for(Method m : mViewType.getMethods()) {
            Class[] paramType = m.getParameterTypes();
            if(m.getName().equals("set"+name) && paramType.length == 1) {
                if(paramType[0].isAssignableFrom(mMemberType) || BindingUtils.isPrimitive(mMemberType, paramType[0])) {
                    mSetter = new BindingSubscriber(m);
                    return;
                }
                if(paramType[0].isAssignableFrom((String.class))) {
                    alt = m;
                }
            }
        }
        if(mGetter.isMember()) {
            mSetter = mGetter;
            return;
        }

        if(alt != null) {
            mSetter = new BindingSubscriber(alt);
            mUsingStringConversion = true;
            return;
        }

        mSetter = new BindingSubscriber(alt);
    }

    private void mapGetter() {
        String name = capitalize(mViewAttr);

        for(Method m : mViewType.getMethods()) {
            if(m.getName().equals("get"+name)) {
                mGetter = new BindingSubscriber(m);
                return;
            }
        }

        for(Field f : mViewType.getFields()) {
            if(f.getName().equals("m"+name)) {
                mGetter = new BindingSubscriber(f);
                return;
            }
        }

        Field f = null;
        mGetter = new BindingSubscriber(f);
    }

    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    private class BindingSubscriber {
        Method mMethod;
        Field mField;

        public BindingSubscriber(Method method) {
            mMethod = method;
        }

        public BindingSubscriber(Field field) {
            mField = field;
        }

        public Object demand(Object target, Object... obj) {
            if (mMethod != null) {
                try {
                    return mMethod.invoke(target, obj);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            } else if (mField != null) {
                try {
                    return mField.get(target);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        public void send(Object target, Object... obj) {
            try {
                if (mMethod != null) {
                    mMethod.invoke(target, obj);
                    return;
                } else if (mField != null) {
                    mField.set(target, obj);
                    return;
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        public boolean isValid() {
            return mMethod != null || mField != null;
        }

        public boolean isMember() {
            return mField != null;
        }
    }
}
