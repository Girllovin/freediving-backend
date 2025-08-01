#!/bin/bash

echo "🔍 Verifying EC2 Application Configuration"
echo "=========================================="

# Check if we're in the right directory
if [ ! -f compose.prod.yaml ]; then
    echo "❌ Error: Not in application directory or compose.prod.yaml not found"
    exit 1
fi

echo "✅ Found compose.prod.yaml"

# Check if containers are running
echo ""
echo "📦 Checking container status..."
if docker-compose -f compose.prod.yaml ps | grep -q "Up"; then
    echo "✅ Containers are running"
else
    echo "❌ Containers are not running"
    echo "Starting containers..."
    docker-compose -f compose.prod.yaml up -d
    sleep 15
fi

# Check which compose file is being used
echo ""
echo "🐳 Docker Compose Configuration:"
echo "Using: compose.prod.yaml"
docker-compose -f compose.prod.yaml config --services

# Check Spring Boot profile using our custom endpoint
echo ""
echo "🌱 Spring Boot Profile Check:"
if curl -s http://localhost:8080/api/config/info | grep -q "prod"; then
    echo "✅ Application is running with 'prod' profile"
    echo "Configuration details:"
    curl -s http://localhost:8080/api/config/info | jq '.' 2>/dev/null || curl -s http://localhost:8080/api/config/info
else
    echo "❌ Application is NOT running with 'prod' profile"
    echo "Expected: application-prod.properties"
    echo "Current configuration:"
    curl -s http://localhost:8080/api/config/info | jq '.' 2>/dev/null || curl -s http://localhost:8080/api/config/info
fi

# Check environment variables
echo ""
echo "🔧 Environment Variables Check:"
echo "Checking if environment variables are loaded..."

# Check specific environment variables
if docker-compose -f compose.prod.yaml exec app env | grep -q "SPRING_PROFILES_ACTIVE=prod"; then
    echo "✅ SPRING_PROFILES_ACTIVE=prod is set"
else
    echo "❌ SPRING_PROFILES_ACTIVE=prod is NOT set"
fi

# Check MongoDB connection
echo ""
echo "🗄️ MongoDB Connection Check:"
if docker-compose -f compose.prod.yaml exec app env | grep -q "MONGODB_URI"; then
    echo "✅ MONGODB_URI is configured"
    docker-compose -f compose.prod.yaml exec app env | grep "MONGODB_URI"
else
    echo "❌ MONGODB_URI is NOT configured"
fi

# Check application properties
echo ""
echo "📄 Application Properties Check:"
echo "Expected files:"
echo "- src/main/resources/application.properties (base)"
echo "- src/main/resources/application-prod.properties (production profile)"

if [ -f src/main/resources/application.properties ]; then
    echo "✅ application.properties found"
else
    echo "❌ application.properties NOT found"
fi

if [ -f src/main/resources/application-prod.properties ]; then
    echo "✅ application-prod.properties found"
else
    echo "❌ application-prod.properties NOT found"
fi

# Check .env file
echo ""
echo "🔐 Environment File Check:"
if [ -f .env ]; then
    echo "✅ .env file found"
    echo "Environment variables in .env:"
    grep -E "(APP_NAME|LOG_LEVEL|MONGODB_URI|MAIL_|CSRF_)" .env || echo "No matching variables found"
else
    echo "❌ .env file NOT found"
fi

# Check application logs for configuration
echo ""
echo "📋 Application Logs (last 20 lines):"
docker-compose -f compose.prod.yaml logs --tail=20 app

# Health check
echo ""
echo "🏥 Health Check:"
if curl -s http://localhost:8080/actuator/health | grep -q "UP"; then
    echo "✅ Application is healthy"
else
    echo "❌ Application health check failed"
fi

echo ""
echo "✅ Configuration verification completed!" 