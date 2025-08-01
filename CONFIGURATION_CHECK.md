# Configuration Verification Guide

This guide explains how to verify that your application on EC2 is using the correct configuration files.

## Quick Verification Commands

### 1. Check if containers are running with correct compose file
```bash
# On EC2 instance
cd /home/ec2-user/app
docker-compose -f compose.prod.yaml ps
```

### 2. Verify Spring Boot profile
```bash
# Check if prod profile is active
curl http://localhost:8080/api/config/info
```

### 3. Check environment variables
```bash
# View environment variables in the container
docker-compose -f compose.prod.yaml exec app env | grep -E "(SPRING_PROFILES_ACTIVE|MONGODB_URI|APP_NAME)"
```

### 4. Run the verification script
```bash
# Make script executable and run
chmod +x verify-config.sh
./verify-config.sh
```

## What to Look For

### ✅ Correct Configuration:
- **Docker Compose**: Using `compose.prod.yaml`
- **Spring Profile**: `prod` profile active
- **Environment**: `.env` file loaded
- **Properties**: `application-prod.properties` being used

### ❌ Wrong Configuration:
- **Docker Compose**: Using default `compose.yaml` instead of `compose.prod.yaml`
- **Spring Profile**: No profile or wrong profile active
- **Environment**: Missing `.env` file
- **Properties**: Using default `application.properties` instead of `application-prod.properties`

## Configuration Files Hierarchy

1. **Base Configuration**: `application.properties`
2. **Profile-Specific**: `application-prod.properties` (overrides base)
3. **Environment Variables**: `.env` file (overrides properties)
4. **Docker Compose**: `compose.prod.yaml` (sets environment variables)

## Verification Endpoints

### Custom Config Endpoint
```bash
curl http://localhost:8080/api/config/info
```
Returns:
```json
{
  "activeProfiles": ["prod"],
  "isProdProfile": true,
  "appName": "freeDive",
  "logLevel": "WARN",
  "csrfEnabled": "true",
  "mongodbUri": "mongodb://root:secret@mongodb:27017/mydatabase?authSource=admin",
  "mailHost": "smtp.gmail.com"
}
```

### Spring Boot Actuator Endpoints
```bash
# Health check
curl http://localhost:8080/actuator/health

# Environment info
curl http://localhost:8080/actuator/env

# Application info
curl http://localhost:8080/actuator/info
```

## Troubleshooting

### If app is not using prod profile:
1. Check `compose.prod.yaml` has `SPRING_PROFILES_ACTIVE=prod`
2. Verify `application-prod.properties` exists
3. Restart containers: `docker-compose -f compose.prod.yaml restart app`

### If environment variables not loaded:
1. Check `.env` file exists and has correct values
2. Verify `compose.prod.yaml` references `.env` file
3. Rebuild containers: `docker-compose -f compose.prod.yaml up --build -d`

### If MongoDB connection fails:
1. Check `MONGODB_URI` in `.env` file
2. Verify MongoDB container is running
3. Check network connectivity between containers

## Manual Configuration Check

You can also manually verify by:

1. **Checking container logs**:
   ```bash
   docker-compose -f compose.prod.yaml logs app
   ```

2. **Inspecting container environment**:
   ```bash
   docker-compose -f compose.prod.yaml exec app env
   ```

3. **Checking mounted files**:
   ```bash
   docker-compose -f compose.prod.yaml exec app ls -la /app/src/main/resources/
   ```

## Expected Configuration Values

### Production Profile (`application-prod.properties`):
- `logging.level.root=INFO`
- `logging.level.org.springframework.security=WARN`
- `spring.security.csrf.enabled=true`
- `server.tomcat.threads.max=200`

### Environment Variables (`.env`):
- `SPRING_PROFILES_ACTIVE=prod`
- `MONGODB_URI=mongodb://root:secret@mongodb:27017/mydatabase?authSource=admin`
- `APP_NAME=freeDive`
- `LOG_LEVEL=INFO`

If any of these values are different, the configuration is not being applied correctly. 