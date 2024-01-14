package ru.msvdev.homefinance.utils;

import java.util.HashMap;
import java.util.Map;

public class HashMapDeDuplicator<T> implements DeDuplicator<T> {

    private final Map<T, T> map = new HashMap<>();


    @Override
    public T deDuplicate(T object) {
        T value = map.putIfAbsent(object, object);
        return (value == null) ? object : value;
    }

    @Override
    public void clear() {
        map.clear();
    }
}
