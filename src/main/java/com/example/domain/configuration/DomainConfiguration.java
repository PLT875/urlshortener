package com.example.domain.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "url.shortener.domain")
@Getter
@Setter
public class DomainConfiguration {
    private String scheme;
    private String domain;
}
