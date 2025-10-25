CREATE SCHEMA IF NOT EXISTS warehouse;
SET SCHEMA 'warehouse';

CREATE TABLE IF NOT EXISTS warehouse_items (
warehouse_item_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
quantity BIGINT NOT NULL DEFAULT 0,
fragile BOOLEAN NOT NULL,
weight DECIMAL NOT NULL,
width DECIMAL NOT NULL,
height DECIMAL NOT NULL,
depth DECIMAL NOT NULL
    );