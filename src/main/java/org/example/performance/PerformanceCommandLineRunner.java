package org.example.performance;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Scanner;

/**
 * Command-line interface for running performance tests manually.
 * This provides an interactive way to trigger and configure performance tests.
 */
@Component
public class PerformanceCommandLineRunner implements CommandLineRunner {
    private final PerformanceLoadTester loadTester;
    private final PerformanceMonitor performanceMonitor;
    private final Scanner scanner = new Scanner(System.in);

    public PerformanceCommandLineRunner(PerformanceLoadTester loadTester, PerformanceMonitor performanceMonitor) {
        this.loadTester = loadTester;
        this.performanceMonitor = performanceMonitor;
    }

    @Override
    public void run(String... args) throws Exception {
        // Only run if launched with "performance" argument
        if (args.length > 0 && "performance".equals(args[0])) {
            runInteractivePerformanceTest();
        }
    }

    private void runInteractivePerformanceTest() {
        try {
            printWelcomeHeader();
            
            // Check if input is available (this is more of a placeholder check)
            // The real check will be in the hasNextLine() below
            
            while (true) {
                printMenu();
                
                // Check if scanner has input available
                if (!scanner.hasNextLine()) {
                    System.out.println("\n❌ No input available. Interactive mode requires direct execution.");
                    System.out.println("💡 Use the shell script for interactive testing:");
                    System.out.println("   ./performance-test.sh quick-test");
                    System.out.println("   ./performance-test.sh custom-test -u 10 -d 60");
                    return;
                }
                
                String choice = scanner.nextLine().trim();
                
                switch (choice) {
                    case "1":
                        runQuickTest();
                        break;
                    case "2":
                        runCustomTest();
                        break;
                    case "3":
                        showCurrentMetrics();
                        break;
                    case "4":
                        resetMetrics();
                        break;
                    case "5":
                        showApiEndpoints();
                        break;
                    case "q":
                    case "Q":
                        System.out.println("\n👋 Thanks for using the Performance Tester! Goodbye!\n");
                        return;
                    default:
                        System.out.println("❌ Invalid choice. Please try again.\n");
                }
            }
        } catch (Exception e) {
            System.out.println("\n❌ Interactive mode failed: " + e.getMessage());
            System.out.println("💡 This typically happens when running through Gradle.");
            System.out.println("💡 Use the shell script commands instead:");
            System.out.println("   ./performance-test.sh quick-test     # Quick test");
            System.out.println("   ./performance-test.sh custom-test -u 10 -d 60  # Custom test");
            System.out.println("   ./performance-test.sh show-metrics  # View current metrics");
        }
    }

    private void printWelcomeHeader() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("🚀 PERFORMANCE TESTING TOOL - REST vs ACTION API COMPARISON");
        System.out.println("=".repeat(80));
        System.out.println("This tool helps you compare the performance of REST and Action-based APIs");
        System.out.println("in your dual-architecture system.\n");
    }

    private void printMenu() {
        System.out.println("📋 Available Options:");
        System.out.println("  1️⃣  Quick Test (5 users, 30 seconds)");
        System.out.println("  2️⃣  Custom Test (configure parameters)");
        System.out.println("  3️⃣  Show Current Metrics");
        System.out.println("  4️⃣  Reset Metrics");
        System.out.println("  5️⃣  Show API Endpoints");
        System.out.println("  Q️⃣  Quit");
        System.out.print("\n🎯 Enter your choice: ");
    }

    private void runQuickTest() {
        System.out.println("\n🚀 Running Quick Performance Test...");
        PerformanceLoadTester.LoadTestConfiguration config = 
            new PerformanceLoadTester.LoadTestConfiguration(5, 30);
        runTest(config);
    }

    private void runCustomTest() {
        System.out.println("\n⚙️  Custom Test Configuration");
        
        int users = getIntInput("Number of concurrent users (1-50)", 5, 1, 50);
        int duration = getIntInput("Test duration in seconds (5-300)", 30, 5, 300);
        
        PerformanceLoadTester.LoadTestConfiguration config = 
            new PerformanceLoadTester.LoadTestConfiguration(users, duration);
        runTest(config);
    }

    private int getIntInput(String prompt, int defaultValue, int min, int max) {
        while (true) {
            System.out.printf("  %s [default: %d]: ", prompt, defaultValue);
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
                return defaultValue;
            }
            
            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.printf("  ❌ Value must be between %d and %d\n", min, max);
                }
            } catch (NumberFormatException e) {
                System.out.println("  ❌ Please enter a valid number");
            }
        }
    }

    private void runTest(PerformanceLoadTester.LoadTestConfiguration config) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🎯 Test Configuration:");
        System.out.println("  • Concurrent Users: " + config.getConcurrentUsers());
        System.out.println("  • Duration: " + config.getTestDurationSeconds() + " seconds");
        System.out.println("=".repeat(60));
        
        // Reset metrics before test
        performanceMonitor.reset();
        System.out.println("🔄 Metrics reset. Starting test...\n");
        
        long startTime = System.currentTimeMillis();
        PerformanceLoadTester.PerformanceTestResult result = loadTester.runLoadTest(config);
        long endTime = System.currentTimeMillis();
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("✅ Test Completed!");
        System.out.println("  • Total Duration: " + (endTime - startTime) + " ms");
        System.out.println("=".repeat(60));
        
        showCurrentMetrics();
    }

    private void showCurrentMetrics() {
        System.out.println("\n📊 Current Performance Metrics:");
        System.out.println("=".repeat(80));
        
        PerformanceComparison comparison = performanceMonitor.getComparison();
        PerformanceMetrics restMetrics = comparison.getRestMetrics();
        PerformanceMetrics actionMetrics = comparison.getActionMetrics();
        
        // REST API Metrics
        System.out.println("🌐 REST API Performance:");
        printMetrics(restMetrics);
        
        System.out.println();
        
        // Action API Metrics
        System.out.println("⚡ Action API Performance:");
        printMetrics(actionMetrics);
        
        System.out.println();
        
        // Comparison
        System.out.println("🔍 Performance Comparison:");
        System.out.println("  • Faster Approach: " + comparison.getFasterApproach());
        System.out.printf("  • Speed Difference: %.2f%% %s%n", 
            Math.abs(comparison.getAverageTimeDifferencePercent()),
            comparison.getAverageTimeDifferencePercent() > 0 ? "(Action faster)" : "(REST faster)");
        System.out.printf("  • Throughput Difference: %.2f%% %s%n", 
            Math.abs(comparison.getThroughputDifferencePercent()),
            comparison.getThroughputDifferencePercent() > 0 ? "(Action higher)" : "(REST higher)");
        System.out.printf("  • Success Rate Difference: %.2f%% %s%n", 
            Math.abs(comparison.getSuccessRateDifferencePercent()),
            comparison.getSuccessRateDifferencePercent() > 0 ? "(Action higher)" : "(REST higher)");
        
        System.out.println("=".repeat(80) + "\n");
    }

    private void printMetrics(PerformanceMetrics metrics) {
        System.out.printf("  • Total Requests: %d%n", metrics.getTotalRequests());
        System.out.printf("  • Average Time: %.2f ms%n", metrics.getAverageExecutionTime());
        System.out.printf("  • Min Time: %d ms%n", metrics.getMinExecutionTime());
        System.out.printf("  • Max Time: %d ms%n", metrics.getMaxExecutionTime());
        System.out.printf("  • Success Rate: %.2f%%%n", metrics.getSuccessRate());
        System.out.printf("  • Throughput: %.2f req/s%n", metrics.getThroughputRequestsPerSecond());
    }

    private void resetMetrics() {
        System.out.println("\n🔄 Resetting all performance metrics...");
        performanceMonitor.reset();
        System.out.println("✅ Metrics reset successfully!\n");
    }

    private void showApiEndpoints() {
        System.out.println("\n🔗 Available REST API Endpoints:");
        System.out.println("=".repeat(80));
        System.out.println("Performance Testing:");
        System.out.println("  • POST /performance/load-test?users=5&duration=30");
        System.out.println("  • GET  /performance/comparison");
        System.out.println("  • GET  /performance/summary");
        System.out.println("  • POST /performance/reset");
        System.out.println();
        System.out.println("REST APIs (for manual testing):");
        System.out.println("  • GET  /api/animals");
        System.out.println("  • POST /api/animals");
        System.out.println("  • GET  /api/cars");
        System.out.println("  • POST /api/cars");
        System.out.println("  • GET  /api/employees");
        System.out.println("  • POST /api/employees");
        System.out.println();
        System.out.println("Action APIs (for manual testing):");
        System.out.println("  • POST /actions/animals/get-all");
        System.out.println("  • POST /actions/animals/create");
        System.out.println("  • POST /actions/cars/get-all");
        System.out.println("  • POST /actions/cars/create");
        System.out.println("  • POST /actions/employees/get-all");
        System.out.println("  • POST /actions/employees/create");
        System.out.println("=".repeat(80) + "\n");
    }
} 