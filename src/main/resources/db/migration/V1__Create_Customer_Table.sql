CREATE TABLE IF NOT EXISTS customer
(
    id                   SERIAL PRIMARY KEY,
    username             VARCHAR(100)  NOT NULL,
    client_id            VARCHAR(1000) NOT NULL,
    synced_on            TIMESTAMP     NULL,
    sync_postponed_until TIMESTAMP     NULL
);