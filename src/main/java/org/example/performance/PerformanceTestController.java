package org.example.performance;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

/**
 * Controller for performance testing and comparison endpoints.
 * 
 * IMPORTANT ARCHITECTURAL DIFFERENCES:
 * - REST API includes HTTP-specific methods (HEAD, OPTIONS) that have no Action equivalent
 * - Action architecture focuses on business operations, not HTTP protocol features
 * - HEAD/OPTIONS testing only applies to REST; Actions test equivalent business logic
 * 
 * COMPARISON SCOPE:
 * ✅ Tested: GET, POST, PUT/PATCH, DELETE, SEARCH, PAGINATION
 * ✅ REST-only: HEAD (existence check), OPTIONS (method discovery)
 * ⚠️  Note: Action architecture achieves similar goals through business operations
 */
@RestController
@RequestMapping("/performance")
public class PerformanceTestController {
    private final PerformanceMonitor performanceMonitor;
    private final PerformanceLoadTester loadTester;

    public PerformanceTestController(PerformanceMonitor performanceMonitor, PerformanceLoadTester loadTester) {
        this.performanceMonitor = performanceMonitor;
        this.loadTester = loadTester;
    }

    /**
     * Get current performance metrics for REST APIs.
     */
    @GetMapping("/rest-metrics")
    public ResponseEntity<PerformanceMetrics> getRestMetrics() {
        return ResponseEntity.ok(performanceMonitor.getMetrics("REST"));
    }

    /**
     * Get current performance metrics for Action APIs.
     */
    @GetMapping("/action-metrics")
    public ResponseEntity<PerformanceMetrics> getActionMetrics() {
        return ResponseEntity.ok(performanceMonitor.getMetrics("ACTION"));
    }

    /**
     * Get performance comparison between REST and Action APIs.
     */
    @GetMapping("/comparison")
    public ResponseEntity<PerformanceComparison> getComparison() {
        return ResponseEntity.ok(performanceMonitor.getComparison());
    }

    /**
     * Reset all performance metrics.
     */
    @PostMapping("/reset")
    public ResponseEntity<String> resetMetrics() {
        performanceMonitor.reset();
        return ResponseEntity.ok("Performance metrics reset successfully");
    }

    /**
     * Get performance summary as text.
     */
    @GetMapping("/summary")
    public ResponseEntity<String> getPerformanceSummary() {
        PerformanceComparison comparison = performanceMonitor.getComparison();
        return ResponseEntity.ok(comparison.toString());
    }

    /**
     * Run a load test manually with custom parameters.
     * @param users Number of concurrent users (default: 5)
     * @param duration Test duration in seconds (default: 30)
     */
    @PostMapping("/load-test")
    public ResponseEntity<String> runLoadTest(
            @RequestParam(defaultValue = "5") int users,
            @RequestParam(defaultValue = "30") int duration) {
        
        PerformanceLoadTester.LoadTestConfiguration config = 
            new PerformanceLoadTester.LoadTestConfiguration(users, duration);
        
        // Reset metrics before test
        performanceMonitor.reset();
        
        // Run the load test
        PerformanceLoadTester.PerformanceTestResult result = loadTester.runLoadTest(config);
        
        // Generate detailed summary with table format
        StringBuilder response = new StringBuilder();
        response.append("🎯 Load Test Completed Successfully!\n\n");
        response.append("📊 Test Configuration:\n");
        response.append("  • Concurrent Users: ").append(config.getConcurrentUsers()).append("\n");
        response.append("  • Test Duration: ").append(config.getTestDurationSeconds()).append(" seconds\n");
        response.append("  • Actual Duration: ").append(result.getDurationMs()).append(" ms\n\n");
        
        // Get fresh comparison data and format as table
        PerformanceComparison comparison = performanceMonitor.getComparison();
        response.append("📈 Performance Results Table:\n");
        response.append(formatAsTable(comparison));
        
        response.append("\n🔗 Access other endpoints:\n");
        response.append("  • Metrics: GET /performance/comparison\n");
        response.append("  • Summary: GET /performance/summary\n");
        response.append("  • Reset: POST /performance/reset\n");
        
        return ResponseEntity.ok(response.toString());
    }

    /**
     * Get performance summary as a formatted table.
     */
    @GetMapping("/table")
    public ResponseEntity<String> getPerformanceTable() {
        PerformanceComparison comparison = performanceMonitor.getComparison();
        return ResponseEntity.ok(formatAsTable(comparison));
    }

    /**
     * Format performance comparison results as a table.
     */
    private String formatAsTable(PerformanceComparison comparison) {
        PerformanceMetrics restMetrics = comparison.getRestMetrics();
        PerformanceMetrics actionMetrics = comparison.getActionMetrics();
        
        StringBuilder table = new StringBuilder();
        
        // Determine winners for each metric
        String speedWinner = comparison.getFasterApproach();
        String throughputWinner = getThroughputWinner(comparison);
        String successWinner = getSuccessRateWinner(comparison);
        
        // Header
        table.append("╔═══════════════╦══════════╦══════════════╦══════════════╦══════════════╦════════╗\n");
        table.append("║ Architecture  ║ Requests ║ Avg Response ║ Success Rate ║ Throughput   ║ Status ║\n");
        table.append("╠═══════════════╬══════════╬══════════════╬══════════════╬══════════════╬════════╣\n");
        
        // REST API row with winner indicators
        String restStatus = getStatusIndicator("REST", speedWinner, throughputWinner, successWinner);
        table.append(String.format("║ %-13s ║ %8s ║ %10.2fms ║ %11.1f%% ║ %9.0f/s ║ %-6s ║\n",
            "REST API",
            formatNumber(restMetrics.getTotalRequests()),
            restMetrics.getAverageExecutionTime(),
            restMetrics.getSuccessRate(),
            restMetrics.getThroughputRequestsPerSecond(),
            restStatus));
        
        // Action API row with winner indicators
        String actionStatus = getStatusIndicator("ACTION", speedWinner, throughputWinner, successWinner);
        table.append(String.format("║ %-13s ║ %8s ║ %10.2fms ║ %11.1f%% ║ %9.0f/s ║ %-6s ║\n",
            "Action API",
            formatNumber(actionMetrics.getTotalRequests()),
            actionMetrics.getAverageExecutionTime(),
            actionMetrics.getSuccessRate(),
            actionMetrics.getThroughputRequestsPerSecond(),
            actionStatus));
        
        // Comparison row
        table.append("╠═══════════════╬══════════╬══════════════╬══════════════╬══════════════╬════════╣\n");
        double requestsDiff = ((double)(actionMetrics.getTotalRequests() - restMetrics.getTotalRequests()) / 
                              Math.max(restMetrics.getTotalRequests(), 1)) * 100;
        table.append(String.format("║ %-13s ║ %+7.1f%% ║ %+9.2f%% ║ %+10.2f%% ║ %+8.1f%% ║ %6s ║\n",
            "Difference",
            requestsDiff,
            -comparison.getAverageTimeDifferencePercent(), // Negative because lower time is better
            comparison.getSuccessRateDifferencePercent(),
            comparison.getThroughputDifferencePercent(),
            ""));
        
        table.append("╚═══════════════╩══════════╩══════════════╩══════════════╩══════════════╩════════╝\n");
        
        // Comprehensive Summary
        table.append("\n🏆 PERFORMANCE SUMMARY:\n");
        table.append("═════════════════════════════════════════════════════════════════════════════════\n");
        
        // Overall winner determination
        String overallWinner = determineOverallWinner(comparison);
        table.append(String.format("🥇 OVERALL WINNER: %s\n", overallWinner));
        
        // Detailed breakdown
        table.append("\n📈 METRIC BREAKDOWN:\n");
        table.append(String.format("  🚀 Speed Champion:      %s (%+.2f%% %s)\n", 
            speedWinner,
            Math.abs(comparison.getAverageTimeDifferencePercent()),
            speedWinner.equals("ACTION") ? "faster" : speedWinner.equals("REST") ? "faster" : "tie"));
            
        table.append(String.format("  ⚡ Throughput Leader:   %s (%+.2f%% %s)\n", 
            throughputWinner,
            Math.abs(comparison.getThroughputDifferencePercent()),
            getThroughputDescription(comparison)));
            
        table.append(String.format("  ✅ Reliability King:    %s (%+.2f%% %s)\n", 
            successWinner,
            Math.abs(comparison.getSuccessRateDifferencePercent()),
            getSuccessRateDescription(comparison)));
        
        // Performance insights
        table.append("\n💡 INSIGHTS:\n");
        if (restMetrics.getTotalRequests() > 0 && actionMetrics.getTotalRequests() > 0) {
            addPerformanceInsights(table, comparison);
        } else {
            table.append("  📊 Run a load test to see detailed performance insights!\n");
        }
        
        return table.toString();
    }

    private String getThroughputWinner(PerformanceComparison comparison) {
        double diff = comparison.getThroughputDifferencePercent();
        if (Math.abs(diff) < 1.0) return "TIE";
        return diff > 0 ? "ACTION" : "REST";
    }

    private String getSuccessRateWinner(PerformanceComparison comparison) {
        double diff = comparison.getSuccessRateDifferencePercent();
        if (Math.abs(diff) < 0.1) return "TIE";
        return diff > 0 ? "ACTION" : "REST";
    }

    private String getStatusIndicator(String architecture, String speedWinner, String throughputWinner, String successWinner) {
        int wins = 0;
        if (speedWinner.equals(architecture)) wins++;
        if (throughputWinner.equals(architecture)) wins++;
        if (successWinner.equals(architecture)) wins++;
        
        switch (wins) {
            case 3: return "🏆🏆🏆";
            case 2: return "🏆🏆";
            case 1: return "🏆";
            default: return "";
        }
    }

    private String determineOverallWinner(PerformanceComparison comparison) {
        String speedWinner = comparison.getFasterApproach();
        String throughputWinner = getThroughputWinner(comparison);
        String successWinner = getSuccessRateWinner(comparison);
        
        // Count wins for each architecture
        int restWins = 0;
        int actionWins = 0;
        
        if ("REST".equals(speedWinner)) restWins++;
        else if ("ACTION".equals(speedWinner)) actionWins++;
        
        if ("REST".equals(throughputWinner)) restWins++;
        else if ("ACTION".equals(throughputWinner)) actionWins++;
        
        if ("REST".equals(successWinner)) restWins++;
        else if ("ACTION".equals(successWinner)) actionWins++;
        
        if (actionWins > restWins) return "ACTION API 🎯";
        else if (restWins > actionWins) return "REST API 🌐";
        else return "TIE 🤝 (Both architectures performed equally)";
    }

    private String getThroughputDescription(PerformanceComparison comparison) {
        double diff = comparison.getThroughputDifferencePercent();
        if (Math.abs(diff) < 1.0) return "tie";
        return diff > 0 ? "higher throughput" : "higher throughput";
    }

    private String getSuccessRateDescription(PerformanceComparison comparison) {
        double diff = comparison.getSuccessRateDifferencePercent();
        if (Math.abs(diff) < 0.1) return "tie";
        return diff > 0 ? "better reliability" : "better reliability";
    }

    private void addPerformanceInsights(StringBuilder table, PerformanceComparison comparison) {
        PerformanceMetrics restMetrics = comparison.getRestMetrics();
        PerformanceMetrics actionMetrics = comparison.getActionMetrics();
        
        // Response time insight
        double avgTimeDiff = comparison.getAverageTimeDifferencePercent();
        if (Math.abs(avgTimeDiff) > 5) {
            String faster = avgTimeDiff > 0 ? "Action" : "REST";
            table.append(String.format("  ⚡ %s API is significantly faster (%.1f%% difference)\n", 
                faster, Math.abs(avgTimeDiff)));
        }
        
        // Throughput insight
        double throughputDiff = comparison.getThroughputDifferencePercent();
        if (Math.abs(throughputDiff) > 10) {
            String leader = throughputDiff > 0 ? "Action" : "REST";
            table.append(String.format("  🚀 %s API handles %.1f%% more requests per second\n", 
                leader, Math.abs(throughputDiff)));
        }
        
        // Consistency insight
        if (restMetrics.getSuccessRate() == 100.0 && actionMetrics.getSuccessRate() == 100.0) {
            table.append("  ✅ Both architectures achieved perfect reliability (100% success)\n");
        }
        
        // Volume insight
        long totalRequests = restMetrics.getTotalRequests() + actionMetrics.getTotalRequests();
        if (totalRequests > 1000) {
            table.append(String.format("  📊 High-volume test: %s total requests processed\n", 
                formatNumber(totalRequests)));
        }
    }

    /**
     * Format numbers with commas for better readability.
     */
    private String formatNumber(long number) {
        return String.format("%,d", number);
    }
} 