package ru.msvdev.homefinance.controller;

public interface DataAccessListener {

    /**
     * Обновление сведений о доступе к файлу с данными
     *
     * @param access true - файл и доступ к данным открыты, false - файл закрыт
     */
    void dataAccessUpdate(boolean access);
}
