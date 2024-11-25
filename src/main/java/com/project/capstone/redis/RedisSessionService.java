package com.project.capstone.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisSessionService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 세션 데이터를 Redis에 저장
    public void saveSession(String sessionId, Object sessionData) {
        redisTemplate.opsForValue().set(sessionId, sessionData);
        // 1시간 동안 유지되도록 설정 (유효기간 설정)
        redisTemplate.expire(sessionId, 1, TimeUnit.HOURS);
    }

    // Redis에서 세션 데이터 조회
    public Object getSession(String sessionId) {
        return redisTemplate.opsForValue().get(sessionId);
    }

    // Redis에서 세션 삭제
    public void deleteSession(String sessionId) {
        redisTemplate.delete(sessionId);
    }
}
