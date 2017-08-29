package com.example.rifar.belanegara.module;

import java.util.ArrayList;

/**
 * Created by asus on 8/15/2017.
 */

public class BindableMember<T> {
    private T mValue;
    private Promise<T> mGetAction;
    private Action<T> mSetAction;
    private ArrayList<BindingExpression> mBinding = new ArrayList<>();

    private BindableMember(Promise<T> get, Action<T> set) { }

    public static <T> BindableMember<T> create() {
        return create(null);
    }

    public static <T> BindableMember<T> create(T value) {
        BindableMember<T> self = create(null);
        self.mValue = value;
        return self;
    }

    public static <T> BindableMember<T> create(Promise<T> get) {
        return create(get, null);
    }

    public static <T> BindableMember<T> create(Promise<T> get, Action<T> set) {
        BindableMember<T> self = new BindableMember<>(get, set);
        self.mGetAction = get;
        self.mSetAction = set;
        return self;
    }

    public T get() {
        updateSource();
        if(!isAuto())
            return mGetAction.invoke();
        else
            return mValue;
    }

    public void set(T value) {
        if(isAuto())
            mValue = value;
        else if(!isReadOnly())
            mSetAction.invoke(value);
        else
            throw new UnsupportedOperationException("BindableMember is read-only");

        updateBind();
    }

    public void createBinding(BindingExpression expr) {
        mBinding.add(expr);
        if(get() != null)
            expr.notify(get());
    }

    public void removeBinding(BindingExpression expr) {
        mBinding.remove(expr);
    }

    public boolean isReadOnly() {
        return mSetAction == null && mValue == null;
    }

    public boolean isAuto() {
        return mGetAction == null && mSetAction == null;
    }

    public Class getType() {
        return mValue.getClass();
    }

    private void updateSource() {
        for(BindingExpression expr : mBinding) {
            if(expr.getMode() == BindingMode.TwoWay || expr.getMode() == BindingMode.OneWayToSource) {
                T fetch = (T) expr.fetch();
                if(isAuto())
                    mValue = fetch;
                else
                    mSetAction.invoke(fetch);
            }
        }
    }

    private void updateBind() {
        for(BindingExpression expr : mBinding) {
            if(expr.getMode() != BindingMode.OneWayToSource && expr.getMode() != BindingMode.OneTime)
                expr.notify(get());
        }
    }
}
