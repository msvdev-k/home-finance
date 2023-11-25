package ru.msvdev.homefinance.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


@Getter
@Component
@PropertySource(value = "classpath:application.yaml", encoding = "UTF-8")
public class AppProperty {

    @Value("${application-name}")
    private String applicationName;

    @Value("${application-version}")
    private String applicationVersion;

    @Value("${application-file-extension}")
    private String applicationFileExtension;

    @Value("${application-file-description}")
    private String applicationFileDescription;
}
