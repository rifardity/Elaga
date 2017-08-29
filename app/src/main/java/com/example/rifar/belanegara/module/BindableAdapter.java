package com.example.rifar.belanegara.module;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by asus on 8/21/2017.
 */

public class BindableAdapter<T> extends BaseAdapter {
    private List<T> mList;
    private int mLayout;
    private LayoutInflater mInflater;
    private BindingApplicator mApplicator;

    public BindableAdapter(Context context, List<T> list) {
        this(context, list, 0);
    }

    public BindableAdapter(Context context, List<T> list, @LayoutRes int layout) {
        mList = list;
        mLayout = layout;
        mInflater = LayoutInflater.from(context);
    }

    public int getLayoutId() {
        return mLayout;
    }

    public void setLayoutId(@LayoutRes int layoutId) {
        mLayout = layoutId;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view != null? view : mInflater.inflate(mLayout, viewGroup, false);
        Object obj = getItem(i);

        if(mApplicator == null)
            mApplicator = new BindingApplicator();
        else
            mApplicator.removeBinding();

        return mApplicator.applyBinding(v, mLayout, obj);
    }

    public void removeBinding() {
        mApplicator.removeBinding();
    }
}
