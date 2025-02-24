CREATE TABLE IF NOT EXISTS payments (
    payment_id      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id        UUID           NOT NULL,
    total_payment   NUMERIC(15, 2) NOT NULL,
    delivery_total  NUMERIC(15, 2) NOT NULL,
    fee_total       NUMERIC(15, 2) NOT NULL,
    payment_state   VARCHAR(50)    NOT NULL
);
create index payments_payment_id_index on payments (payment_id);
create index payments_order_id_index on payments (order_id);