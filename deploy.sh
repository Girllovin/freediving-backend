#!/bin/bash

# Exit on any error
set -e

echo "Starting deployment..."

# Navigate to the application directory
cd /home/ec2-user/app

# Stop existing containers if running
echo "Stopping existing containers..."
docker-compose -f compose.prod.yaml down || true

# Pull latest changes (if using git)
echo "Pulling latest changes..."
git pull origin main || true

# Create .env file if it doesn't exist
if [ ! -f .env ]; then
    echo "Creating .env file from example..."
    cp env.example .env
    echo "Please update .env file with your production values!"
fi

# Verify configuration files exist
echo "Verifying configuration files..."
if [ ! -f compose.prod.yaml ]; then
    echo "ERROR: compose.prod.yaml not found!"
    exit 1
fi

if [ ! -f src/main/resources/application-prod.properties ]; then
    echo "ERROR: application-prod.properties not found!"
    exit 1
fi

if [ ! -f .env ]; then
    echo "ERROR: .env file not found!"
    exit 1
fi

echo "✅ All configuration files found"

# Show which configuration files will be used
echo "Configuration files that will be used:"
echo "- Docker Compose: compose.prod.yaml"
echo "- Spring Profile: prod (application-prod.properties)"
echo "- Environment: .env"

# Build and start containers using production compose
echo "Building and starting containers..."
docker-compose -f compose.prod.yaml up --build -d

# Wait for services to be healthy
echo "Waiting for services to be ready..."
sleep 30

# Check if containers are running
echo "Checking container status..."
docker-compose -f compose.prod.yaml ps

# Verify the app is using the correct profile
echo "Verifying Spring Boot profile..."
sleep 10
if curl -f http://localhost:8080/actuator/env | grep -q "prod"; then
    echo "✅ Application is running with 'prod' profile"
else
    echo "❌ Application is NOT running with 'prod' profile"
    docker-compose -f compose.prod.yaml logs app
fi

# Check application health
echo "Checking application health..."
sleep 10
if curl -f http://localhost:8080/actuator/health; then
    echo "✅ Application is healthy!"
else
    echo "❌ Application health check failed!"
    docker-compose -f compose.prod.yaml logs app
    exit 1
fi

# Show environment variables being used
echo "Environment variables being used:"
docker-compose -f compose.prod.yaml exec app env | grep -E "(APP_NAME|LOG_LEVEL|MONGODB_URI|MAIL_|CSRF_)" || echo "No environment variables found"

echo "Deployment completed successfully!" 