#!/bin/bash
set -e

echo "🚀 Starting MySQL + LocalStack..."
docker compose up -d

echo "⏳ Waiting MySQL..."
until docker exec mysql-account mysqladmin ping -h localhost -proot --silent; do
  sleep 2
done

echo "⏳ Waiting LocalStack..."
until curl -s http://localhost:4566/_localstack/health | grep -q "\"sqs\""; do
  sleep 2
done

echo "📦 Creating SQS queue: transactions-queue"
aws --endpoint-url=http://localhost:4566 sqs create-queue --queue-name transactions-queue --region us-east-1 --no-sign-request >/dev/null || true

echo "📋 Listing queues:"
aws --endpoint-url=http://localhost:4566 sqs list-queues --region us-east-1 --no-sign-request

echo "✅ Dev environment ready."

