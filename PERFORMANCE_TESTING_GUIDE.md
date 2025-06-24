# 🚀 Performance Testing Guide

Detailed guide for using the performance testing tools to compare REST vs Action API architectures.

## 🎯 Test Commands Reference

### Shell Script Commands
| Command | Description | Example |
|---------|-------------|---------|
| `quick-test` | Quick test (5 users, 30s) | `./performance-test.sh quick-test` |
| `custom-test` | Custom test parameters | `./performance-test.sh custom-test -u 10 -d 60` |
| `web-test` | Same as custom-test (alias) | `./performance-test.sh web-test -u 8 -d 45` |
| `interactive` | Launch CLI menu | `./performance-test.sh interactive` |
| `show-metrics` | Display current results | `./performance-test.sh show-metrics` |
| `reset-metrics` | Reset all counters | `./performance-test.sh reset-metrics` |
| `start-app` | Start Spring Boot app | `./performance-test.sh start-app` |
| `help` | Show help message | `./performance-test.sh help` |

### Shell Script Options
| Option | Description | Default |
|--------|-------------|---------|
| `-u, --users` | Concurrent users | 5 |
| `-d, --duration` | Test duration (seconds) | 30 |
| `-p, --port` | Application port | 8080 |

### REST API Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/performance/load-test?users=5&duration=30` | Trigger load test |
| `GET` | `/performance/comparison` | Detailed JSON comparison |
| `GET` | `/performance/summary` | Formatted text summary |
| `GET` | `/performance/table` | Performance table view |
| `GET` | `/performance/rest-metrics` | REST API metrics only |
| `GET` | `/performance/action-metrics` | Action API metrics only |
| `POST` | `/performance/reset` | Reset all metrics |

## 📊 Understanding Results

### Metrics Explained
- **Total Requests**: API calls processed during the test
- **Average Time**: Mean response time (ms) 
- **Success Rate**: Percentage of successful requests (%)
- **Throughput**: Requests processed per second
- **Status**: Trophy indicators (🏆) for winning metrics
- **Difference**: Percentage comparison (Action vs REST)

### Sample Results
```
╔═══════════════╦══════════╦══════════════╦══════════════╦══════════════╦════════╗
║ Architecture  ║ Requests ║ Avg Response ║ Success Rate ║ Throughput   ║ Status ║
╠═══════════════╬══════════╬══════════════╬══════════════╬══════════════╬════════╣
║ REST API      ║    1,250 ║      12.45ms ║       99.2%  ║      42/s    ║        ║
║ Action API    ║    1,340 ║       9.73ms ║       99.9%  ║      45/s    ║ 🏆🏆🏆  ║
╠═══════════════╬══════════╬══════════════╬══════════════╬══════════════╬════════╣
║ Difference    ║   +7.2%  ║     -21.8%   ║      +0.7%   ║     +7.1%    ║        ║
╚═══════════════╩══════════╩══════════════╩══════════════╩══════════════╩════════╝

🏆 PERFORMANCE SUMMARY:
═════════════════════════════════════════════════════════════════════════════════
🥇 OVERALL WINNER: ACTION API 🎯

📈 METRIC BREAKDOWN:
  🚀 Speed Champion:      ACTION (+21.8% faster)
  ⚡ Throughput Leader:   ACTION (+7.1% higher throughput)
  ✅ Reliability King:    ACTION (+0.7% better reliability)

💡 INSIGHTS:
  ⚡ Action API is significantly faster (21.8% difference)
  📊 High-volume test: 2,590 total requests processed
```

## 🔧 Testing Strategy

### Recommended Test Scenarios
1. **Light Load**: 3-5 users, 15-30 seconds (baseline)
2. **Medium Load**: 8-15 users, 30-60 seconds (normal usage)
3. **Heavy Load**: 20-50 users, 60-120 seconds (stress test)

### Best Practices
- Start with light load for stability validation
- Reset metrics between test scenarios
- Run multiple iterations for consistency
- Monitor system resources during tests

### Example Testing Session
```bash
# Light load baseline
./performance-test.sh custom-test -u 3 -d 30
./performance-test.sh show-metrics

# Medium load test
./performance-test.sh reset-metrics
./performance-test.sh custom-test -u 10 -d 60
./performance-test.sh show-metrics

# Heavy load stress test
./performance-test.sh reset-metrics
./performance-test.sh custom-test -u 25 -d 90
./performance-test.sh show-metrics
```

## 🐛 Troubleshooting

### Common Issues & Solutions

**Application won't start**
```bash
# Check port usage
lsof -i :8080
# Kill existing process
kill -9 <PID>
```

**Connection refused errors**
- Ensure application is running: `./gradlew bootRun`
- Check port configuration
- Verify database connection

**Poor performance results**
- Check system resources (CPU, memory)
- Verify database performance
- Reduce concurrent users
- Check for other running processes

**Test failures**
- Check application logs
- Verify database constraints
- Review timeout settings
- Check network connectivity

## 🎮 Interactive Mode Features

The interactive CLI provides a menu-driven interface:
- Quick preset tests
- Custom parameter configuration
- Real-time metrics display
- Help and documentation
- Easy metric reset

Launch with: `./performance-test.sh interactive`

## 🎪 Demo Script

For a guided introduction to the system capabilities, use:
```bash
./demo-performance.sh    # Interactive system walkthrough
```

This **demo script**:
- ✅ Shows available commands and features
- ✅ Explains endpoints and capabilities  
- ✅ Provides guided examples
- ❌ Does NOT run actual performance tests
- ❌ Does NOT generate load or metrics

The **demo is educational only** - use `performance-test.sh` for actual testing.

---

For basic usage, see the main `README.md`. For architectural details, see `ARCHITECTURE_GUIDE.md`. 