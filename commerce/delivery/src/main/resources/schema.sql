CREATE TABLE addresses (
    address_id  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    country     VARCHAR(100)    NOT NULL,
    city        VARCHAR(100)    NOT NULL,
    street      VARCHAR(255)    NOT NULL,
    house       VARCHAR(5)      NOT NULL,
    flat        VARCHAR(5)
);
create index addresses_address_id_index on addresses (address_id);

CREATE TABLE deliveries (
    delivery_id     UUID PRIMARY KEY,
    from_address_id UUID        NOT NULL,
    to_address_id   UUID        NOT NULL,
    order_id        UUID        NOT NULL,
    delivery_state  VARCHAR(20) NOT NULL,
    delivery_weight DOUBLE      NOT NULL,
    delivery_volume DOUBLE      NOT NULL,
    fragile         BOOLEAN     NOT NULL,
    FOREIGN KEY (from_address_id) REFERENCES addresses (address_id),
    FOREIGN KEY (to_address_id) REFERENCES addresses (address_id)
);
create index deliveries_delivery_id_index on deliveries (delivery_id);
create index deliveries_from_address_id_index on deliveries (from_address_id);
create index deliveries_to_address_id_index on deliveries (to_address_id);
create index deliveries_order_id_index on deliveries (order_id);