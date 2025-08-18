package com.charlie.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Component
public class RateLimiter {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 限流检查
     * @param key 限流key
     * @param maxCount 最大请求数
     * @param timeWindow 时间窗口（秒）
     * @return 是否允许通过
     */
    public boolean isAllowed(String key, int maxCount, int timeWindow) {
        String redisKey = "rate_limit:" + key;
        String currentCount = redisTemplate.opsForValue().get(redisKey);

        if (currentCount == null) {
            // 第一次请求，设置计数器
            redisTemplate.opsForValue().set(redisKey, "1", timeWindow, TimeUnit.SECONDS);
            return true;
        } else {
            // 增加计数器
            Long count = redisTemplate.opsForValue().increment(redisKey, 1);
            return count <= maxCount;
        }
    }
}

