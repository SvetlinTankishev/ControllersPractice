package org.example.performance;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Interceptor to automatically track REST API performance.
 */
@Component
public class PerformanceInterceptor implements HandlerInterceptor {
    private final PerformanceMonitor performanceMonitor;
    private static final String START_TIME_ATTR = "startTime";

    public PerformanceInterceptor(PerformanceMonitor performanceMonitor) {
        this.performanceMonitor = performanceMonitor;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Only track REST API calls (not action-based ones)
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/api/")) {
            request.setAttribute(START_TIME_ATTR, System.currentTimeMillis());
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        // This method is intentionally empty as we handle timing in afterCompletion
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/api/") && request.getAttribute(START_TIME_ATTR) != null) {
            long startTime = (Long) request.getAttribute(START_TIME_ATTR);
            long executionTime = System.currentTimeMillis() - startTime;
            boolean success = response.getStatus() < 400;
            
            performanceMonitor.recordRestExecution(executionTime, success);
        }
    }
} 