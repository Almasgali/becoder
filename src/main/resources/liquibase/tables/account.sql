--changeset PetyaVasya:create-account-table
CREATE TABLE IF NOT EXISTS account (
    id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    balance BIGINT NOT NULL DEFAULT 0 CHECK (balance >= 0)
);
