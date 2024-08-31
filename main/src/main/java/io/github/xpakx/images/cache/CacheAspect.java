package io.github.xpakx.images.cache;

import io.github.xpakx.images.cache.annotation.CacheDecrement;
import io.github.xpakx.images.cache.annotation.CacheIncrement;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
@Service
@RequiredArgsConstructor
public class CacheAspect {
    private final CacheManager cacheManager;

    @Pointcut("@annotation(cacheIncrement)")
    public void cacheIncrementPointcut(CacheIncrement cacheIncrement) {
    }

    @Pointcut("@annotation(cacheDecrement)")
    public void cacheDecrementPointcut(CacheDecrement cacheDecrement) {
    }

    @AfterReturning(value = "cacheIncrementPointcut(cacheIncrement)", argNames = "joinPoint,cacheIncrement")
    public void incrementAfterReturning(JoinPoint joinPoint, CacheIncrement cacheIncrement) {
        String cacheName = cacheIncrement.value();
        String keyExpression = cacheIncrement.key();
        System.out.println("INCREMENT");
        System.out.println("CACHE NAME:" + cacheName);
        System.out.println("CACHE KEY:" + keyExpression);
    }

    @AfterReturning(value = "cacheDecrementPointcut(cacheDecrement)", argNames = "joinPoint,cacheDecrement")
    public void decrementAfterReturning(JoinPoint joinPoint, CacheDecrement cacheDecrement) {
        String cacheName = cacheDecrement.value();
        String keyExpression = cacheDecrement.key();
        System.out.println("DECREMENT");
        System.out.println("CACHE NAME:" + cacheName);
        System.out.println("CACHE KEY:" + keyExpression);
    }
}
