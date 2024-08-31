package io.github.xpakx.images.cache;

import io.github.xpakx.images.cache.annotation.CacheDecrement;
import io.github.xpakx.images.cache.annotation.CacheDecrements;
import io.github.xpakx.images.cache.annotation.CacheIncrement;
import io.github.xpakx.images.cache.annotation.CacheIncrements;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Objects;

@Aspect
@Service
@RequiredArgsConstructor
public class CacheAspect {
    Logger logger = LoggerFactory.getLogger(CacheAspect.class);
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
        String key = parseKey(joinPoint, cacheIncrement.key());
        logger.debug("Increment {} in cache {}", key, cacheName);
        updateCache(cacheName, key, 1);
    }

    @AfterReturning(value = "cacheDecrementPointcut(cacheDecrement)", argNames = "joinPoint,cacheDecrement")
    public void decrementAfterReturning(JoinPoint joinPoint, CacheDecrement cacheDecrement) {
        String cacheName = cacheDecrement.value();
        String key = parseKey(joinPoint, cacheDecrement.key());
        logger.debug("Decrement {} in cache {}", key, cacheName);
        updateCache(cacheName, key, -1);
    }

    private String parseKey(JoinPoint joinPoint, String keyExpression) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();

        var parameterNames = signature.getParameterNames();
        var args = joinPoint.getArgs();
        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }
        return parser.parseExpression(keyExpression) .getValue(context, String.class);
    }

    private void updateCache(String cacheName, String key, int delta) {
        var cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            Long currentLikeCount = cache.get(key, Long.class);
            if (currentLikeCount == null) {
                return; // TODO ?
            }
            cache.put( key, currentLikeCount + delta );
        }
    }


    @Pointcut("@annotation(cacheIncrements)")
    public void cacheIncrementsContainerPointcut(CacheIncrements cacheIncrements) {
    }

    @AfterReturning(value = "cacheIncrementsContainerPointcut(cacheIncrements)", argNames = "joinPoint,cacheIncrements")
    public void incrementAfterReturning(JoinPoint joinPoint, CacheIncrements cacheIncrements) {
        CacheIncrement[] increments = cacheIncrements.value();
        for (var increment : increments) {
            incrementAfterReturning(joinPoint, increment);
        }
    }

    @Pointcut("@annotation(cacheDecrements)")
    public void cacheDecrementsContainerPointcut(CacheDecrements cacheDecrements) {
    }

    @AfterReturning(value = "cacheDecrementsContainerPointcut(cacheDecrements)", argNames = "joinPoint,cacheDecrements")
    public void decrementAfterReturning(JoinPoint joinPoint, CacheDecrements cacheDecrements) {
        CacheDecrement[] decrements = cacheDecrements.value();
        for (var decrement : decrements) {
            decrementAfterReturning(joinPoint, decrement);
        }
    }
}