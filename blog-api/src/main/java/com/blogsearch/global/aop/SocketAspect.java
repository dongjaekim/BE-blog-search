package com.blogsearch.global.aop;

import com.blogsearch.global.exception.BlogSearchException;
import com.blogsearch.global.exception.BlogSearchExceptionType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
public class SocketAspect {

    @Pointcut("execution(* com.blogsearch.utils.client..*.searchBlogResults(..))")
    private void callSearchApi(){}

    @Around("callSearchApi()")
    public Object catchSocketException(ProceedingJoinPoint joinPoint) throws Throwable {
        String signature = joinPoint.getSignature().toShortString();
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            String localizedMessage = e.getCause().getLocalizedMessage();
            log.info("[{}] exception caused by: {}", signature, localizedMessage);

            if (localizedMessage.contains("Connect"))
                throw new BlogSearchException(BlogSearchExceptionType.CONNECT_TIME_OUT, localizedMessage);
            else if (localizedMessage.contains("Read"))
                throw new BlogSearchException(BlogSearchExceptionType.READ_TIME_OUT, localizedMessage);

            throw e;
        }
    }
}