package org.example.performance;

/**
 * Comparison of performance metrics between REST and Action-based APIs.
 */
public class PerformanceComparison {
    private final PerformanceMetrics restMetrics;
    private final PerformanceMetrics actionMetrics;

    public PerformanceComparison(PerformanceMetrics restMetrics, PerformanceMetrics actionMetrics) {
        this.restMetrics = restMetrics;
        this.actionMetrics = actionMetrics;
    }

    public PerformanceMetrics getRestMetrics() {
        return restMetrics;
    }

    public PerformanceMetrics getActionMetrics() {
        return actionMetrics;
    }

    /**
     * Calculate performance difference as percentage (positive means Action is faster).
     */
    public double getAverageTimeDifferencePercent() {
        if (restMetrics.getAverageExecutionTime() == 0) return 0;
        double restAvg = restMetrics.getAverageExecutionTime();
        double actionAvg = actionMetrics.getAverageExecutionTime();
        return ((restAvg - actionAvg) / restAvg) * 100;
    }

    /**
     * Get throughput difference (positive means Action has higher throughput).
     */
    public double getThroughputDifferencePercent() {
        if (restMetrics.getThroughputRequestsPerSecond() == 0) return 0;
        double restThroughput = restMetrics.getThroughputRequestsPerSecond();
        double actionThroughput = actionMetrics.getThroughputRequestsPerSecond();
        return ((actionThroughput - restThroughput) / restThroughput) * 100;
    }

    /**
     * Get success rate difference (positive means Action has higher success rate).
     */
    public double getSuccessRateDifferencePercent() {
        return actionMetrics.getSuccessRate() - restMetrics.getSuccessRate();
    }

    /**
     * Get winner based on average execution time.
     */
    public String getFasterApproach() {
        if (actionMetrics.getAverageExecutionTime() < restMetrics.getAverageExecutionTime()) {
            return "ACTION";
        } else if (restMetrics.getAverageExecutionTime() < actionMetrics.getAverageExecutionTime()) {
            return "REST";
        } else {
            return "TIE";
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Performance Comparison ===\n");
        sb.append("REST API: ").append(restMetrics.toString()).append("\n");
        sb.append("Action API: ").append(actionMetrics.toString()).append("\n");
        sb.append("Average Time Difference: ").append(String.format("%.2f%%", getAverageTimeDifferencePercent()));
        sb.append(" (positive means Action is faster)\n");
        sb.append("Throughput Difference: ").append(String.format("%.2f%%", getThroughputDifferencePercent()));
        sb.append(" (positive means Action has higher throughput)\n");
        sb.append("Success Rate Difference: ").append(String.format("%.2f%%", getSuccessRateDifferencePercent()));
        sb.append(" (positive means Action has higher success rate)\n");
        sb.append("Faster Approach: ").append(getFasterApproach()).append("\n");
        return sb.toString();
    }
} 