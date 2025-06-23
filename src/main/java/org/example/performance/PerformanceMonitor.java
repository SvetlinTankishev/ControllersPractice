package org.example.performance;

import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Performance monitor for tracking REST vs Action-based API performance.
 */
@Component
public class PerformanceMonitor {
    private final Map<String, PerformanceMetrics> metrics = new ConcurrentHashMap<>();

    public PerformanceMonitor() {
        metrics.put("REST", new PerformanceMetrics("REST"));
        metrics.put("ACTION", new PerformanceMetrics("ACTION"));
    }

    /**
     * Records execution time for REST API calls.
     */
    public void recordRestExecution(long executionTimeMs, boolean success) {
        metrics.get("REST").recordExecution(executionTimeMs, success);
    }

    /**
     * Records execution time for Action-based API calls.
     */
    public void recordActionExecution(long executionTimeMs, boolean success) {
        metrics.get("ACTION").recordExecution(executionTimeMs, success);
    }

    /**
     * Gets performance metrics for the specified API type.
     */
    public PerformanceMetrics getMetrics(String apiType) {
        return metrics.get(apiType);
    }

    /**
     * Gets performance comparison between REST and Action APIs.
     */
    public PerformanceComparison getComparison() {
        PerformanceMetrics restMetrics = metrics.get("REST");
        PerformanceMetrics actionMetrics = metrics.get("ACTION");
        return new PerformanceComparison(restMetrics, actionMetrics);
    }

    /**
     * Resets all performance metrics.
     */
    public void reset() {
        metrics.put("REST", new PerformanceMetrics("REST"));
        metrics.put("ACTION", new PerformanceMetrics("ACTION"));
    }
} 