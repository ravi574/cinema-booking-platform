package com.cinema.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class SeatLockService {
    private final StringRedisTemplate redis;

    public SeatLockService(StringRedisTemplate r) {
        this.redis = r;
    }

    public boolean lock(String key) {
        return Boolean.TRUE.equals(redis.opsForValue().setIfAbsent(key, "LOCK", Duration.ofMinutes(5)));
    }

    public void release(String key) {
        redis.delete(key);
    }
}