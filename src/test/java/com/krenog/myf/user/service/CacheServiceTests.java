package com.krenog.myf.user.service;

import com.krenog.myf.user.services.cache.CacheService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.krenog.myf.user.UserTestUtils.TEST_VALUE;
import static com.krenog.myf.utils.TestUtils.TEST_PHONE_NUMBER;

@SpringBootTest
@Tag("RealTest")
public class CacheServiceTests {

    @Autowired
    private CacheService cacheService;

    @BeforeEach
    void setUp() {
        cacheService.deleteValue(TEST_VALUE);
    }

    @Test
    void setCacheTest() {
        cacheService.setValue(TEST_PHONE_NUMBER, TEST_VALUE);
    }

    @Test
    void getCacheTest() {
        cacheService.setValue(TEST_PHONE_NUMBER, TEST_VALUE);
        String cacheValue = cacheService.getValue(TEST_PHONE_NUMBER);
        Assertions.assertEquals(TEST_VALUE, cacheValue);
    }

    @Test
    void incrementCacheTest() {
        cacheService.setValue(TEST_PHONE_NUMBER, TEST_VALUE);
        cacheService.incrementValue(TEST_PHONE_NUMBER);
        String cacheValue = cacheService.getValue(TEST_PHONE_NUMBER);
        Assertions.assertEquals("1", cacheValue);
    }

    @Test
    void deleteCacheTest() {
        cacheService.setValue(TEST_PHONE_NUMBER, TEST_VALUE);
        cacheService.deleteValue(TEST_PHONE_NUMBER);
        String cacheValue = cacheService.getValue(TEST_PHONE_NUMBER);
        Assertions.assertNull(cacheValue);
    }

}
