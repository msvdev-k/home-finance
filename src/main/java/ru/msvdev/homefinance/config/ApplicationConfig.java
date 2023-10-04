package ru.msvdev.homefinance.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@ComponentScan({"ru.msvdev.homefinance"})
@EnableTransactionManagement(proxyTargetClass = true)
public class ApplicationConfig {
}
