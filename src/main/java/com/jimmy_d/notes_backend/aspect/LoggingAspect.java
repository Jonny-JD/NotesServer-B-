package com.jimmy_d.notes_backend.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("@within(org.springframework.stereotype.Service)")
    public void isServiceLayer() {
    }

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void isControllerLayer() {
    }

    private String maskSensitiveData(Object[] args) {
        return Arrays.stream(args)
                .map(arg -> {
                    if (arg == null) return "null";

                    String str = arg.toString();
                    // простая эвристика — можно заменить на анализ полей
                    if (str.toLowerCase().contains("password")) {
                        return "password: *******";
                    }
                    return str;
                })
                .toList()
                .toString();
    }

    @Around("isServiceLayer() || isControllerLayer()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        var methodName = joinPoint.getSignature().toShortString();
        var args = joinPoint.getArgs();
        log.info("Invoking method: {} with args: {}", methodName, maskSensitiveData(args));
        long start = System.currentTimeMillis();

        try {
            var result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - start;
            log.info("Method {} completed successfully in {} ms", methodName, duration);
            return result;
        } catch (Throwable throwable) {
            long duration = System.currentTimeMillis() - start;
            log.error("Method {} threw an exception after {} ms: {}", methodName, duration, throwable.toString());
            throw throwable;
        }
    }
}
