--liquibase formatted sql

--changeset PetyaVasya:create-account-table
CREATE TABLE IF NOT EXISTS account (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    balance BIGINT NOT NULL DEFAULT 0 CHECK (balance >= 0)
);

--changeset PetyaVasya:add-security-fields
ALTER TABLE account
    ADD COLUMN IF NOT EXISTS password VARCHAR(200) NOT NULL,
    ADD COLUMN IF NOT EXISTS role VARCHAR(30) NOT NULL;

--changeset PetyaVasya:add-comments runOnChange:true
COMMENT ON TABLE account IS 'Данные об аккаунте пользователя, включая имя и баланс счета';
COMMENT ON COLUMN account.role IS 'Роль пользователя в системе';
