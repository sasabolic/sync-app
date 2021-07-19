CREATE TABLE IF NOT EXISTS data_object
(
    id          VARCHAR(18) PRIMARY KEY NOT NULL,
    customer_id INTEGER                 NOT NULL,
    created_on  TIMESTAMP               NOT NULL,
    modified_on TIMESTAMP               NOT NULL,
    state       VARCHAR(50)             NOT NULL,
    type        VARCHAR(50)             NOT NULL,
    raw_data    TEXT                    NULL,
    CONSTRAINT fk_data_object_customer_id
        FOREIGN KEY (customer_id)
            REFERENCES customer (id)
);