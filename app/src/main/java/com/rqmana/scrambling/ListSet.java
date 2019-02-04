package com.rqmana.scrambling;

import java.util.ArrayList;
import java.util.Collection;

public class ListSet<T> extends ArrayList<T> {

    @Override
    public boolean add(T element) {
        if (isExist(element))
            return false;
        return super.add(element);
    }

    @Override
    public void add(int index, T element) {
        if (!isExist(element))
            super.add(index, element);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> collection) {
        for (T element : collection)
            if (isExist(element))
                return false;
        return super.addAll(index, collection);
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        for (T element : collection)
            if (isExist(element))
                return false;
        return super.addAll(collection);
    }

    private boolean isExist(T element) {
        if (element == null) return false;
        for (int i = 0; i < size(); i++) {
            if (element.equals(get(i))) {
                return true;
            }
        }
        return false;
    }
}
