#!/bin/bash

set -e

echo "🚀 Starting LocalStack (SQS only)..."

docker run -d \
  --name localstack \
  -p 4566:4566 \
  -e SERVICES=sqs \
  -e AWS_DEFAULT_REGION=us-east-1 \
  -e EDGE_PORT=4566 \
  localstack/localstack:latest

echo "⏳ Waiting for LocalStack to be ready..."
sleep 8

echo "📦 Creating SQS queue: transactions-queue"

aws --endpoint-url=http://localhost:4566 \
    sqs create-queue \
    --queue-name transactions-queue \
    --region us-east-1 \
    --profile default 2>/dev/null || \
aws --endpoint-url=http://localhost:4566 \
    sqs create-queue \
    --queue-name transactions-queue \
    --region us-east-1 \
    --no-sign-request

echo "📋 Listing queues:"
aws --endpoint-url=http://localhost:4566 \
    sqs list-queues \
    --region us-east-1 \
    --no-sign-request

echo "✅ LocalStack is ready."

