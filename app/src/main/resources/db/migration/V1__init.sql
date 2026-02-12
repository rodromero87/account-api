CREATE TABLE accounts (
  id BINARY(16) NOT NULL PRIMARY KEY,
  owner_id BINARY(16) NOT NULL,
  balance_amount DECIMAL(19,2) NOT NULL,
  balance_currency VARCHAR(3) NOT NULL,
  created_at TIMESTAMP(6) NOT NULL,
  updated_at TIMESTAMP(6) NOT NULL
);

CREATE TABLE transactions (
  id BINARY(16) NOT NULL PRIMARY KEY,
  account_id BINARY(16) NOT NULL,
  type VARCHAR(20) NOT NULL,
  amount DECIMAL(19,2) NOT NULL,
  currency VARCHAR(3) NOT NULL,
  status VARCHAR(20) NOT NULL,
  timestamp TIMESTAMP(6) NOT NULL,
  INDEX idx_tx_account_ts (account_id, timestamp),
  INDEX idx_tx_status (status)
);
