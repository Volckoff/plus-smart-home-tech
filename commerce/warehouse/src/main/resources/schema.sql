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

CREATE TABLE IF NOT EXISTS booking (
booking_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
order_id UUID NOT NULL,
delivery_id UUID
    );

CREATE TABLE IF NOT EXISTS booking_products (
booking_id UUID NOT NULL,
product_id UUID NOT NULL,
quantity INTEGER NOT NULL,
CONSTRAINT booking_products_pk PRIMARY KEY (booking_id, product_id),
CONSTRAINT booking_products_fk FOREIGN KEY (booking_id) REFERENCES booking(booking_id) ON DELETE CASCADE
    );