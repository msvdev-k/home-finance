package ru.msvdev.homefinance.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapDeDuplicator<T> implements DeDuplicator<T> {

    private final Map<T, T> map = new ConcurrentHashMap<>();


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
