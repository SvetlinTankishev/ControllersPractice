# Controllers Practice - Dual Architecture Demo

A Spring Boot application demonstrating **REST vs Action-based APIs** side-by-side with comprehensive performance testing tools.

## 🚀 Quick Start

```bash
# 1. Start the application
./gradlew bootRun

# 2. Run performance test
./performance-test.sh quick-test

# 3. View results
./performance-test.sh show-metrics
```

## 🏗️ Architecture Overview

### Two API Patterns in One App

**REST Pattern** → `/api/{resource}`
```bash
GET  /api/cars           # Get all cars
POST /api/cars           # Create car
GET  /api/cars/search    # Search cars
```

**Action Pattern** → `/actions/{resource}/{action}`
```bash
POST /actions/cars/get-all    # Get all cars
POST /actions/cars/create     # Create car  
POST /actions/cars/search     # Search cars
```

### Shared Infrastructure
- Same **Service Layer** (`CarService`, `AnimalService`, `GovEmployeeService`)
- Same **Database** (MySQL with Flyway migrations)
- Same **Business Logic** - only the API layer differs

## 📊 Performance Testing

### Built-in Performance Tools
- **Automatic Monitoring**: Both patterns tracked in real-time
- **Load Testing**: Simulates concurrent users
- **Comparison Engine**: Side-by-side performance analysis

### Available Commands
```bash
# Quick tests
./performance-test.sh quick-test              # 5 users, 30 seconds
./performance-test.sh custom-test -u 10 -d 60 # Custom params
./performance-test.sh interactive             # Menu interface

# Results
./performance-test.sh show-metrics            # Current stats
./performance-test.sh reset-metrics           # Reset counters
```

### REST API Access
```bash
# Trigger test
curl -X POST "localhost:8080/performance/load-test?users=5&duration=30"

# Get results
curl "localhost:8080/performance/summary"
curl "localhost:8080/performance/table"
```

## 🎯 Key Features

### Performance Features
✅ **Real-time Metrics**: Response times, throughput, success rates  
✅ **Automated Load Testing**: Multi-threaded concurrent testing  
✅ **Visual Comparisons**: Formatted tables and summaries  
✅ **Multiple Interfaces**: Shell script, REST API, interactive CLI  

### Architecture Features  
✅ **Type-Safe Actions**: Strongly typed request/response models  
✅ **Central Dispatching**: Single point for all action routing  
✅ **Comprehensive Logging**: Built-in execution tracking  
✅ **Full Test Coverage**: Unit, integration, and API tests  

## 📈 Sample Output

```
🏆 PERFORMANCE SUMMARY:
═════════════════════════════════════════════════════════════════════════════════
🥇 OVERALL WINNER: ACTION API 🎯

📈 METRIC BREAKDOWN:
  🚀 Speed Champion:      ACTION (+21.86% faster)
  ⚡ Throughput Leader:   ACTION (+7.20% higher throughput)  
  ✅ Reliability King:    ACTION (+0.65% better reliability)
```

## 🛠️ Tech Stack

- **Framework**: Spring Boot 3.4.7, Java 21
- **Database**: MySQL with Flyway migrations
- **Testing**: JUnit 5, TestContainers, MockMvc
- **Performance**: Custom monitoring with concurrent metrics collection
- **Documentation**: Comprehensive guides and examples

## 📚 Detailed Guides

- `ARCHITECTURE_GUIDE.md` - Comprehensive guide to both REST and Action patterns
- `PERFORMANCE_TESTING_GUIDE.md` - Complete testing documentation
- `IMPLEMENTATION_SUMMARY.md` - Technical implementation details

## 🎪 Demo Mode

```bash
./demo-performance.sh    # Interactive system demonstration
```

### Demo vs Testing Scripts
- **`demo-performance.sh`**: Guided walkthrough showing system capabilities (no actual testing)
- **`performance-test.sh`**: Full testing tool with actual load generation and metrics

---

**Ready to compare architectures?** Start with `./performance-test.sh quick-test` and see which pattern performs better! 🏁 