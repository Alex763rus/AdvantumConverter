package com.example.advantumconverter.aspect;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Log4j2
public class MetricsAspect {
    @Around("@annotation(logExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint, LogExecutionTime logExecutionTime) throws Throwable {
        long startTime = System.nanoTime();

        try {
            return joinPoint.proceed();
        } finally {
            long endTime = System.nanoTime();
            long durationNs = endTime - startTime;

            String methodName = getMethodName(joinPoint);
            String formattedDuration = formatDuration(durationNs, logExecutionTime.unit());

            log.info("Процесс [{}] выполнен за [{}], метод [{}]", logExecutionTime.value(), formattedDuration, methodName);
        }
    }

    private String getMethodName(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();
        return className + "." + methodName + "()";
    }

    private String formatDuration(long nanoseconds, LogExecutionTime.TimeUnit unit) {
        return String.format(unit.getFormat(), nanoseconds / unit.getDividend());
    }
}
