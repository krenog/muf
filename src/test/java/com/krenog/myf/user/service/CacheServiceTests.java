package com.krenog.myf.user.service;

import com.krenog.myf.user.services.cache.CacheService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CacheServiceTests {
    private static final String VALUE = "0";
    private static final String PHONE_NUMBER = "7999999999";
    @Autowired
    private CacheService cacheService;
    @BeforeEach
    void setUp() {
        cacheService.deleteValue(VALUE);
    }

    @Test
    void setCacheTest() {
        cacheService.setValue(PHONE_NUMBER, VALUE);
    }

    @Test
    void getCacheTest() {
        cacheService.setValue(PHONE_NUMBER, VALUE);
        String cacheValue = cacheService.getValue(PHONE_NUMBER);
        Assertions.assertEquals(VALUE, cacheValue);
    }

    @Test
    void incrementCacheTest() {
        cacheService.setValue(PHONE_NUMBER, VALUE);
        cacheService.incrementValue(PHONE_NUMBER);
        String cacheValue = cacheService.getValue(PHONE_NUMBER);
        Assertions.assertEquals("1", cacheValue);
    }

    @Test
    void deleteCacheTest() {
        cacheService.setValue(PHONE_NUMBER, VALUE);
        cacheService.deleteValue(PHONE_NUMBER);
        String cacheValue = cacheService.getValue(PHONE_NUMBER);
        Assertions.assertNull(  cacheValue);
    }

}
