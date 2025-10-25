CREATE SCHEMA IF NOT EXISTS shopping_cart;
SET SCHEMA 'shopping_cart';

CREATE TABLE IF NOT EXISTS shopping_carts (
shopping_cart_id uuid default gen_random_uuid() primary key,
user_name varchar(255) not null,
is_active boolean not null default true
    );

CREATE TABLE IF NOT EXISTS shopping_cart_products (
product_id uuid not null,
shopping_cart_id uuid not null references shopping_carts(shopping_cart_id),
quantity bigint not null,
primary key (product_id, shopping_cart_id)
    );