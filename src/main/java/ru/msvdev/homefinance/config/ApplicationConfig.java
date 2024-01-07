package ru.msvdev.homefinance.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.msvdev.desktop.utils.data.EnableJpaEntities;
import ru.msvdev.desktop.utils.data.EnableJpaRepositories;
import ru.msvdev.homefinance.MainApplication;


@Configuration
@ComponentScan(basePackageClasses = {MainApplication.class})
@EnableJpaEntities("ru.msvdev.homefinance.data.entity")
@EnableJpaRepositories("ru.msvdev.homefinance.data.repository")
public class ApplicationConfig {
}
