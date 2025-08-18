package com.charlie.aspect;

import com.charlie.annotation.RateLimit;
import com.charlie.config.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class RateLimitAspect {

    @Autowired
    private RateLimiter rateLimiter;

    @Around("@annotation(rateLimit)")
    public Object around(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String key = request.getRequestURI() + ":" + getClientIP(request);

        if (!rateLimiter.isAllowed(key, rateLimit.value(), rateLimit.timeWindow())) {
            throw new RuntimeException("请求过于频繁，请稍后重试");
        }

        return joinPoint.proceed();
    }

    private String getClientIP(HttpServletRequest request) {
        String xip = request.getHeader("X-Real-IP");
        String xfor = request.getHeader("X-Forwarded-For");
        if (xfor != null && !"unKnown".equalsIgnoreCase(xfor)) {
            int index = xfor.indexOf(",");
            if (index != -1) {
                return xfor.substring(0, index);
            } else {
                return xfor;
            }
        }
        xfor = xip;
        if (xfor != null && !"unKnown".equalsIgnoreCase(xfor)) {
            return xfor;
        }
        return request.getRemoteAddr();
    }
}
