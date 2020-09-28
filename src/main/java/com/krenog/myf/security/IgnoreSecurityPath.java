package com.krenog.myf.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "com.krenog.filter")
public class IgnoreSecurityPath {

    private List<String> excluded = new ArrayList<>();

    @Bean
    public List<String> excludedPaths() {
        return excluded;
    }


    public List<String> getExcluded() {
        return excluded;
    }

    public void setExcluded(List<String> excludedPaths) {
        this.excluded = excludedPaths;
    }
}
