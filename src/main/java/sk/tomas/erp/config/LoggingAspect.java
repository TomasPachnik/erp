package sk.tomas.erp.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Around("@within(sk.tomas.erp.annotations.MyLogger) || @annotation(sk.tomas.erp.annotations.MyLogger)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object proceed = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - start;

        log.debug(joinPoint.getSignature() + " with input: " + Arrays.toString(joinPoint.getArgs()) + " and output: " +
                proceed + " executed in " + executionTime + "ms");
        return proceed;
    }
}
