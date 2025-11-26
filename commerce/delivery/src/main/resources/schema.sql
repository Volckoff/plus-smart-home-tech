CREATE SCHEMA IF NOT EXISTS delivery;
SET search_path TO delivery;

CREATE TABLE IF NOT EXISTS address (
address_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
country varchar(20) NOT NULL,
city varchar(50) NOT NULL,
street varchar(50) NOT NULL,
house varchar(20) NOT NULL,
flat varchar(20)
    );

CREATE TABLE IF NOT EXISTS delivery (
delivery_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
order_id UUID NOT NULL,
from_address_id UUID NOT NULL,
to_address_id UUID NOT NULL,
state varchar(20) NOT NULL,
delivery_weight DOUBLE PRECISION,
delivery_volume DOUBLE PRECISION,
fragile BOOLEAN,

CONSTRAINT delivery_from_address_fk FOREIGN KEY (from_address_id) REFERENCES address(address_id) ON DELETE CASCADE,
CONSTRAINT delivery_to_address_fk FOREIGN KEY (to_address_id) REFERENCES address(address_id) ON DELETE CASCADE
    );