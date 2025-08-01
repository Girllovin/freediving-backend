# Deployment Guide

This guide explains how to deploy the FreeDive application to Amazon EC2 using GitHub Actions.

## Prerequisites

### 1. EC2 Instance Setup
- Launch an EC2 instance (Amazon Linux 2 or Ubuntu recommended)
- Configure security groups to allow SSH (port 22) and HTTP (port 8080)
- Install Docker and Docker Compose on the EC2 instance

### 2. GitHub Repository Secrets
Add the following secrets to your GitHub repository:
- `EC2_HOST`: Your EC2 instance public IP or domain
- `EC2_USER`: SSH username (usually `ec2-user` for Amazon Linux, `ubuntu` for Ubuntu)
- `EC2_SSH_KEY`: Your private SSH key for connecting to EC2

## EC2 Setup Commands

Run these commands on your EC2 instance:

```bash
# Update system
sudo yum update -y  # For Amazon Linux
# or
sudo apt update && sudo apt upgrade -y  # For Ubuntu

# Install Docker
sudo yum install -y docker  # For Amazon Linux
# or
sudo apt install docker.io  # For Ubuntu

# Start and enable Docker
sudo systemctl start docker
sudo systemctl enable docker

# Add user to docker group
sudo usermod -aG docker $USER

# Install Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# Create application directory
mkdir -p /home/ec2-user/app
```

## Environment Configuration

1. Copy `env.example` to `.env` on your EC2 instance
2. Update the `.env` file with your production values:

```bash
# On EC2 instance
cd /home/ec2-user/app
cp env.example .env
nano .env  # Edit with your production values
```

## GitHub Actions Setup

The workflow will automatically:
1. Copy files to EC2
2. Run the deployment script
3. Build and start containers
4. Verify deployment

## Manual Deployment

If you need to deploy manually:

```bash
# On EC2 instance
cd /home/ec2-user/app
chmod +x deploy.sh
./deploy.sh
```

## Monitoring

- Application logs: `docker-compose -f compose.prod.yaml logs app`
- MongoDB logs: `docker-compose -f compose.prod.yaml logs mongodb`
- Container status: `docker-compose -f compose.prod.yaml ps`

## Troubleshooting

### Common Issues:

1. **Port 8080 not accessible**
   - Check EC2 security group allows inbound traffic on port 8080
   - Verify application is running: `docker-compose -f compose.prod.yaml ps`

2. **MongoDB connection issues**
   - Check MongoDB container is running
   - Verify environment variables in `.env` file

3. **Build failures**
   - Check Docker has enough memory (at least 2GB recommended)
   - Verify all required files are present

### Health Check

The application exposes health endpoints:
- Health check: `http://your-ec2-ip:8080/actuator/health`
- Application info: `http://your-ec2-ip:8080/actuator/info`

## Security Considerations

1. **Environment Variables**: Never commit `.env` file to git
2. **SSH Keys**: Use key-based authentication for EC2
3. **Firewall**: Configure security groups properly
4. **Updates**: Keep Docker and system packages updated

## Scaling

For production scaling, consider:
- Using AWS RDS for MongoDB instead of containerized MongoDB
- Setting up a load balancer
- Using AWS ECS or EKS for container orchestration
- Implementing proper monitoring and logging solutions 