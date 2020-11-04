package com.krenog.myf.user.services.cache;

public interface CacheService {
    String getValue(String key) throws NullPointerException;

    void setValue(String key, String value);

    void setExpire(String key, long seconds);

    void deleteValue(String key);

    void incrementValue(String key);
}
