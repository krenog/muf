package com.krenog.myf.services.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class CacheServiceImpl implements CacheService {
    private final RedisTemplate redisTemplate;
    @Value("${redis.prefix}")
    private String prefix;

    @Autowired
    public CacheServiceImpl(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public String getValue(String key) throws NullPointerException {
        try {
            return redisTemplate.opsForValue().get(prefix + key).toString();
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public void incrementValue(String key) throws NullPointerException, NumberFormatException {
        String cacheData = getValue(key);
        int data = Integer.parseInt(cacheData);
        setValue(key, String.valueOf(data + 1));
    }

    @Override
    public void deleteValue(String key) {
        redisTemplate.delete(prefix + key);
    }


    @Override
    public void setValue(String key, String value) {
        redisTemplate.opsForValue().set(prefix + key, value);
    }

    @Override
    public void setExpire(String key, long seconds) {
        redisTemplate.expire(prefix + key, seconds, TimeUnit.SECONDS);
    }
}
