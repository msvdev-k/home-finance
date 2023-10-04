package ru.msvdev.homefinance.controller;


import java.io.IOException;

public interface ShowUtilityWindow {

    /**
     * Отобразить окно на экране и передать ему управление
     */
    void show() throws IOException;

    /**
     * Закрыть окно
     */
    void close();
}
