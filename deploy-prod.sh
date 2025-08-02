#!/bin/bash

# Production deployment script for freediving backend
set -e

echo "🚀 Starting production deployment..."

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker is not running. Please start Docker first."
    exit 1
fi

# Check if .env file exists
if [ ! -f .env ]; then
    echo "❌ .env file not found. Please create it with your environment variables."
    exit 1
fi

echo "📦 Building and starting production services..."
docker-compose -f compose.prod.yaml down
docker-compose -f compose.prod.yaml build --no-cache
docker-compose -f compose.prod.yaml up -d

echo "⏳ Waiting for services to start..."
sleep 30

echo "🔍 Checking application health..."
for i in {1..10}; do
    if curl -f http://localhost:8080/actuator/health > /dev/null 2>&1; then
        echo "✅ Application is healthy!"
        break
    else
        echo "⏳ Waiting for application to be ready... (attempt $i/10)"
        sleep 10
    fi
done

if [ $i -eq 10 ]; then
    echo "❌ Application failed to start properly"
    docker-compose -f compose.prod.yaml logs app
    exit 1
fi

echo "✅ Production deployment completed successfully!"
echo "🌐 Application is available at: http://localhost:8080"
echo "📊 Health check: http://localhost:8080/actuator/health"
echo "📚 API docs: http://localhost:8080/swagger-ui/index.html" 