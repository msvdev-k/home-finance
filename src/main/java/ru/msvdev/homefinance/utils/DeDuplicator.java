package ru.msvdev.homefinance.utils;

public interface DeDuplicator<T> {

    T deDuplicate(T object);

    void clear();

}
