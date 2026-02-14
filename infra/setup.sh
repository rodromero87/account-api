#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
COMPOSE_FILE="${SCRIPT_DIR}/docker-compose.yml"

AWS_REGION="us-east-1"
QUEUE_NAME="transactions-queue"
DLQ_NAME="transactions-queue-dlq"

echo "==> Derrubando stack antiga"
docker compose -f "$COMPOSE_FILE" down -v --remove-orphans || true

echo "==> Subindo containers"
docker compose -f "$COMPOSE_FILE" up -d --build

echo "==> Aguardando LocalStack"
for i in {1..60}; do
  if docker exec localstack sh -lc "awslocal sqs list-queues --region $AWS_REGION >/dev/null 2>&1"; then
    break
  fi
  sleep 2
done

echo "==> Criando DLQ"
docker exec localstack sh -lc \
  "awslocal sqs create-queue --queue-name $DLQ_NAME --region $AWS_REGION >/dev/null 2>&1 || true"

DLQ_URL=$(docker exec localstack sh -lc \
  "awslocal sqs get-queue-url --queue-name $DLQ_NAME --region $AWS_REGION --query 'QueueUrl' --output text")

DLQ_ARN=$(docker exec localstack sh -lc \
  "awslocal sqs get-queue-attributes --queue-url $DLQ_URL --attribute-names QueueArn --region $AWS_REGION --query 'Attributes.QueueArn' --output text")

REDRIVE_POLICY="{\"deadLetterTargetArn\":\"$DLQ_ARN\",\"maxReceiveCount\":\"3\"}"

echo "==> Criando fila principal"
docker exec localstack sh -lc \
  "awslocal sqs create-queue \
  --queue-name $QUEUE_NAME \
  --attributes RedrivePolicy='$REDRIVE_POLICY' \
  --region $AWS_REGION >/dev/null 2>&1 || true"

echo
echo "===================================="
echo "Aplicação pronta:"
echo "API:        http://localhost:8080"
echo "Simulator:  http://localhost:8099"
echo "Prometheus: http://localhost:9090"
echo "Grafana:    http://localhost:3000"
echo "Loki:       http://localhost:3100"
echo "LocalStack: http://localhost:4566"
echo "===================================="

