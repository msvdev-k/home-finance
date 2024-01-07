package ru.msvdev.homefinance;

import ru.msvdev.desktop.utils.DesktopApplication;
import ru.msvdev.homefinance.config.ApplicationConfig;


public class MainApplication {

    public static void main(String[] args) {
        DesktopApplication.run(args, ApplicationConfig.class);
    }

}