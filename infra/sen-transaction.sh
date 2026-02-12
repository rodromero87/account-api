#!/bin/bash

# ===============================
# CONFIG
# ===============================

export AWS_ACCESS_KEY_ID=test
export AWS_SECRET_ACCESS_KEY=test
export AWS_REGION=us-east-1

ENDPOINT=http://localhost:4566
QUEUE_NAME=transactions-queue
ACCOUNT_ID="5b19c8b6-0cc4-4c72-a989-0c2ee15fa975"
OWNER_ID="315e3cfe-f4af-4cd2-b298-a449e614349a"

# ===============================
# CREATE QUEUE (if not exists)
# ===============================

echo "Ensuring queue exists..."

aws --endpoint-url=$ENDPOINT \
    sqs create-queue \
    --queue-name $QUEUE_NAME \
    --region $AWS_REGION \
    > /dev/null 2>&1

QUEUE_URL=$(aws --endpoint-url=$ENDPOINT \
    sqs get-queue-url \
    --queue-name $QUEUE_NAME \
    --region $AWS_REGION \
    --query 'QueueUrl' \
    --output text)

echo "Queue URL: $QUEUE_URL"

# ===============================
# BUILD PAYLOAD
# ===============================

TRANSACTION_ID=$(uuidgen)

cat > payload.json <<EOF
{
  "transaction": {
    "id": "$TRANSACTION_ID",
    "type": "CREDIT",
    "amount": 97.07,
    "currency": "BRL",
    "status": "APPROVED",
    "timestamp": 1770922376
  },
  "account": {
    "id": "$ACCOUNT_ID",
    "owner": "$OWNER_ID",
    "created_at": "1634874339",
    "status": "ENABLED",
    "balance": {
      "amount": 183.12,
      "currency": "BRL"
    }
  }
}
EOF

# ===============================
# SEND MESSAGE
# ===============================

echo "Sending message..."

aws --endpoint-url=$ENDPOINT \
    sqs send-message \
    --queue-url $QUEUE_URL \
    --message-body file://payload.json \
    --region $AWS_REGION

echo "Message sent successfully."

