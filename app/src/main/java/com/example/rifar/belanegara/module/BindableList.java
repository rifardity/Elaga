package com.example.rifar.belanegara.module;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by asus on 8/21/2017.
 */

public class BindableList<T> implements List<T> {
    private List<T> mList;
    private BindableAdapter<T> mAdapter;

    private BindableList() { }

    public void atttachAdapter(BindableAdapter<T> adapter) {
        mAdapter = adapter;
    }

    public void detachAdapter() {
        mAdapter.removeBinding();
        mAdapter = null;
    }

    public static <T> BindableList<T> create() {
        BindableList<T> self = new BindableList<>();
        self.mList = new ArrayList<>();
        return self;
    }

    @Override
    public int size() {
        return mList.size();
    }

    @Override
    public boolean isEmpty() {
        return mList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return mList.contains(o);
    }

    @NonNull
    @Override
    public Iterator<T> iterator() {
        return mList.iterator();
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return mList.toArray();
    }

    @NonNull
    @Override
    public <T> T[] toArray(@NonNull T[] t1s) {
        return mList.toArray(t1s);
    }

    @Override
    public boolean add(T t) {
        boolean rt = mList.add(t);
        notifyAdapter();
        return rt;
    }

    @Override
    public boolean remove(Object o) {
        boolean rt =  mList.remove(o);
        notifyAdapter();
        return rt;
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> collection) {
        return mList.containsAll(collection);
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends T> collection) {
        boolean rt =  mList.addAll(collection);
        notifyAdapter();
        return rt;
    }

    @Override
    public boolean addAll(int i, @NonNull Collection<? extends T> collection) {
        boolean rt = mList.addAll(i, collection);
        notifyAdapter();
        return rt;
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> collection) {
        boolean rt = mList.removeAll(collection);
        notifyAdapter();
        return rt;
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> collection) {
        return mList.retainAll(collection);
    }

    @Override
    public void clear() {
        mList.clear();
        notifyAdapter();
    }

    @Override
    public T get(int i) {
        return mList.get(i);
    }

    @Override
    public T set(int i, T t) {
        T rt = mList.set(i, t);
        notifyAdapter();
        return rt;
    }

    @Override
    public void add(int i, T t) {
        mList.add(i, t);
        notifyAdapter();
    }

    @Override
    public T remove(int i) {
        T rt = mList.remove(i);
        notifyAdapter();
        return rt;
    }

    @Override
    public int indexOf(Object o) {
        return mList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return mList.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return mList.listIterator();
    }

    @NonNull
    @Override
    public ListIterator<T> listIterator(int i) {
        return mList.listIterator(i);
    }

    @NonNull
    @Override
    public List<T> subList(int i, int i1) {
        return mList.subList(i, i1);
    }

    private void notifyAdapter() {
        if(mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }
}
