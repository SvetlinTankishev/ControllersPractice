package org.example.performance;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Performance metrics for tracking API execution statistics.
 */
public class PerformanceMetrics {
    private final String apiType;
    private final AtomicLong totalRequests = new AtomicLong(0);
    private final AtomicLong totalExecutionTime = new AtomicLong(0);
    private final AtomicLong successfulRequests = new AtomicLong(0);
    private final AtomicLong failedRequests = new AtomicLong(0);
    private volatile long minExecutionTime = Long.MAX_VALUE;
    private volatile long maxExecutionTime = Long.MIN_VALUE;
    private final LocalDateTime startTime;

    public PerformanceMetrics(String apiType) {
        this.apiType = apiType;
        this.startTime = LocalDateTime.now();
    }

    public void recordExecution(long executionTimeMs, boolean success) {
        totalRequests.incrementAndGet();
        totalExecutionTime.addAndGet(executionTimeMs);
        
        if (success) {
            successfulRequests.incrementAndGet();
        } else {
            failedRequests.incrementAndGet();
        }

        synchronized (this) {
            if (executionTimeMs < minExecutionTime) {
                minExecutionTime = executionTimeMs;
            }
            if (executionTimeMs > maxExecutionTime) {
                maxExecutionTime = executionTimeMs;
            }
        }
    }

    public String getApiType() {
        return apiType;
    }

    public long getTotalRequests() {
        return totalRequests.get();
    }

    public double getAverageExecutionTime() {
        long total = totalRequests.get();
        return total > 0 ? (double) totalExecutionTime.get() / total : 0.0;
    }

    public long getMinExecutionTime() {
        return minExecutionTime == Long.MAX_VALUE ? 0 : minExecutionTime;
    }

    public long getMaxExecutionTime() {
        return maxExecutionTime == Long.MIN_VALUE ? 0 : maxExecutionTime;
    }

    public long getSuccessfulRequests() {
        return successfulRequests.get();
    }

    public long getFailedRequests() {
        return failedRequests.get();
    }

    public double getSuccessRate() {
        long total = totalRequests.get();
        return total > 0 ? (double) successfulRequests.get() / total * 100 : 0.0;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public double getThroughputRequestsPerSecond() {
        long duration = java.time.Duration.between(startTime, LocalDateTime.now()).toSeconds();
        return duration > 0 ? (double) totalRequests.get() / duration : 0.0;
    }

    @Override
    public String toString() {
        return String.format(
            "PerformanceMetrics{apiType='%s', totalRequests=%d, avgTime=%.2fms, minTime=%dms, maxTime=%dms, successRate=%.2f%%, throughput=%.2f req/s}",
            apiType, getTotalRequests(), getAverageExecutionTime(), getMinExecutionTime(), 
            getMaxExecutionTime(), getSuccessRate(), getThroughputRequestsPerSecond()
        );
    }
} 