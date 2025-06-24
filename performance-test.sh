#!/bin/bash

# Performance Testing Script for Dual-Architecture System
# This script provides convenient ways to trigger and monitor performance tests

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Default values
DEFAULT_USERS=5
DEFAULT_DURATION=30
DEFAULT_PORT=8080

# Print header
print_header() {
    echo -e "${BLUE}"
    echo "═══════════════════════════════════════════════════════════════════════════════"
    echo "🚀 DUAL-ARCHITECTURE PERFORMANCE TESTING TOOL"
    echo "   REST vs Action-Based API Performance Comparison"
    echo "═══════════════════════════════════════════════════════════════════════════════"
    echo -e "${NC}"
}

# Print usage
print_usage() {
    echo "Usage: $0 [COMMAND] [OPTIONS]"
    echo ""
    echo "Commands:"
    echo "  quick-test      Run a quick performance test (5 users, 30 seconds)"
    echo "  custom-test     Run a custom performance test with parameters"
    echo "  interactive     Launch interactive command-line tool"
    echo "  web-test        Trigger test via REST API"
    echo "  show-metrics    Display current performance metrics"
    echo "  reset-metrics   Reset all performance metrics"
    echo "  start-app       Start the Spring Boot application"
    echo "  help           Show this help message"
    echo ""
    echo "Options for custom-test:"
    echo "  -u, --users     Number of concurrent users (default: $DEFAULT_USERS)"
    echo "  -d, --duration  Test duration in seconds (default: $DEFAULT_DURATION)"
    echo "  -p, --port      Application port (default: $DEFAULT_PORT)"
    echo ""
    echo "Examples:"
    echo "  $0 quick-test"
    echo "  $0 custom-test -u 10 -d 60"
    echo "  $0 web-test --users 8 --duration 45"
    echo "  $0 interactive"
}

# Check if application is running
check_app_running() {
    local port=${1:-$DEFAULT_PORT}
    if curl -s "http://localhost:$port/actuator/health" > /dev/null 2>&1; then
        return 0
    else
        return 1
    fi
}

# Wait for application to be ready
wait_for_app() {
    local port=${1:-$DEFAULT_PORT}
    local max_attempts=30
    local attempt=1
    
    echo -e "${YELLOW}⏳ Waiting for application to be ready on port $port...${NC}"
    
    while [ $attempt -le $max_attempts ]; do
        if check_app_running $port; then
            echo -e "${GREEN}✅ Application is ready!${NC}"
            return 0
        fi
        
        echo -n "."
        sleep 2
        attempt=$((attempt + 1))
    done
    
    echo -e "${RED}❌ Application failed to start within $(($max_attempts * 2)) seconds${NC}"
    return 1
}

# Start the Spring Boot application
start_application() {
    echo -e "${BLUE}🚀 Starting Spring Boot Application...${NC}"
    
    if [ ! -f "./gradlew" ]; then
        echo -e "${RED}❌ gradlew not found. Please run this script from the project root directory.${NC}"
        exit 1
    fi
    
    echo "Building and starting the application..."
    ./gradlew bootRun &
    
    wait_for_app
}

# Run quick test via REST API
run_web_test() {
    local users=${1:-$DEFAULT_USERS}
    local duration=${2:-$DEFAULT_DURATION}
    local port=${3:-$DEFAULT_PORT}
    
    if ! check_app_running $port; then
        echo -e "${RED}❌ Application is not running on port $port${NC}"
        echo "Please start the application first with: $0 start-app"
        exit 1
    fi
    
    echo -e "${BLUE}🌐 Triggering performance test via REST API...${NC}"
    echo "Parameters: Users=$users, Duration=$duration seconds"
    echo ""
    
    curl -X POST "http://localhost:$port/performance/load-test?users=$users&duration=$duration" \
        -H "Content-Type: application/json" \
        -w "\n\nHTTP Status: %{http_code}\nTotal Time: %{time_total}s\n" \
        2>/dev/null || {
        echo -e "${RED}❌ Failed to connect to the application${NC}"
        exit 1
    }
}

# Show current metrics
show_metrics() {
    local port=${1:-$DEFAULT_PORT}
    
    if ! check_app_running $port; then
        echo -e "${RED}❌ Application is not running on port $port${NC}"
        exit 1
    fi
    
    echo -e "${BLUE}📊 Current Performance Metrics:${NC}"
    echo ""
    
    curl -s "http://localhost:$port/performance/table" || {
        echo -e "${RED}❌ Failed to fetch metrics${NC}"
        exit 1
    }
    echo ""
}

# Reset metrics
reset_metrics() {
    local port=${1:-$DEFAULT_PORT}
    
    if ! check_app_running $port; then
        echo -e "${RED}❌ Application is not running on port $port${NC}"
        exit 1
    fi
    
    echo -e "${BLUE}🔄 Resetting performance metrics...${NC}"
    
    result=$(curl -s -X POST "http://localhost:$port/performance/reset")
    echo "$result"
    echo -e "${GREEN}✅ Metrics reset completed${NC}"
}

# Launch interactive mode
launch_interactive() {
    echo -e "${BLUE}🎮 Interactive Performance Testing Menu${NC}"
    echo ""
    
    while true; do
        echo "📋 Available Options:"
        echo "  1️⃣  Quick Test (5 users, 30 seconds)"
        echo "  2️⃣  Custom Test (configure parameters)"
        echo "  3️⃣  Show Current Metrics"
        echo "  4️⃣  Reset Metrics"
        echo "  5️⃣  Show API Endpoints"
        echo "  Q️⃣  Quit"
        echo ""
        read -p "🎯 Enter your choice: " choice
        
        case $choice in
            1)
                echo -e "${BLUE}🚀 Running Quick Test...${NC}"
                run_web_test 5 30 $DEFAULT_PORT
                ;;
            2)
                echo -e "${BLUE}⚙️ Custom Test Configuration${NC}"
                read -p "Number of concurrent users [default: 5]: " users
                read -p "Test duration in seconds [default: 30]: " duration
                users=${users:-5}
                duration=${duration:-30}
                run_web_test $users $duration $DEFAULT_PORT
                ;;
            3)
                show_metrics $DEFAULT_PORT
                ;;
            4)
                reset_metrics $DEFAULT_PORT
                ;;
            5)
                show_api_endpoints
                ;;
            q|Q)
                echo -e "${GREEN}👋 Thanks for using the Performance Tester! Goodbye!${NC}"
                break
                ;;
            *)
                echo -e "${RED}❌ Invalid choice. Please try again.${NC}"
                ;;
        esac
        echo ""
        read -p "Press Enter to continue..."
        echo ""
    done
}

# Show API endpoints
show_api_endpoints() {
    echo -e "${BLUE}🔗 Available REST API Endpoints:${NC}"
    echo "Performance Testing:"
    echo "  • POST /performance/load-test?users=5&duration=30"
    echo "  • GET  /performance/comparison"
    echo "  • GET  /performance/summary"
    echo "  • GET  /performance/table"
    echo "  • POST /performance/reset"
    echo ""
    echo "REST APIs (for manual testing):"
    echo "  • GET  /api/animals"
    echo "  • POST /api/animals"
    echo "  • GET  /api/cars"
    echo "  • POST /api/cars"
    echo "  • GET  /api/employees"
    echo "  • POST /api/employees"
    echo ""
    echo "Action APIs (for manual testing):"
    echo "  • POST /actions/animals/get-all"
    echo "  • POST /actions/animals/create"
    echo "  • POST /actions/cars/get-all"
    echo "  • POST /actions/cars/create"
    echo "  • POST /actions/employees/get-all"
    echo "  • POST /actions/employees/create"
}

# Parse command line arguments
parse_args() {
    USERS=$DEFAULT_USERS
    DURATION=$DEFAULT_DURATION
    PORT=$DEFAULT_PORT
    
    while [[ $# -gt 0 ]]; do
        case $1 in
            -u|--users)
                USERS="$2"
                shift 2
                ;;
            -d|--duration)
                DURATION="$2"
                shift 2
                ;;
            -p|--port)
                PORT="$2"
                shift 2
                ;;
            *)
                shift
                ;;
        esac
    done
}

# Main script logic
main() {
    print_header
    
    if [ $# -eq 0 ]; then
        print_usage
        exit 0
    fi
    
    case $1 in
        quick-test)
            shift
            parse_args "$@"
            run_web_test $DEFAULT_USERS $DEFAULT_DURATION $PORT
            ;;
        custom-test)
            shift
            parse_args "$@"
            run_web_test $USERS $DURATION $PORT
            ;;
        web-test)
            shift
            parse_args "$@"
            run_web_test $USERS $DURATION $PORT
            ;;
        interactive)
            launch_interactive
            ;;
        show-metrics)
            shift
            parse_args "$@"
            show_metrics $PORT
            ;;
        reset-metrics)
            shift
            parse_args "$@"
            reset_metrics $PORT
            ;;
        start-app)
            start_application
            ;;
        help|--help|-h)
            print_usage
            ;;
        *)
            echo -e "${RED}❌ Unknown command: $1${NC}"
            echo ""
            print_usage
            exit 1
            ;;
    esac
}

# Run main function
main "$@" 