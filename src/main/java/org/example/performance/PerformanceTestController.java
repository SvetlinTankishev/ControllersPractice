package org.example.performance;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

/**
 * Controller for performance testing and comparison endpoints.
 */
@RestController
@RequestMapping("/performance")
public class PerformanceTestController {
    private final PerformanceMonitor performanceMonitor;

    public PerformanceTestController(PerformanceMonitor performanceMonitor) {
        this.performanceMonitor = performanceMonitor;
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
} 