CREATE INDEX ON customer (synced_on NULLS FIRST);

CREATE INDEX ON data_object (id, state);

CREATE INDEX ON data_object (customer_id, state);