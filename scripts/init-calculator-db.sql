CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS operation (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    type VARCHAR(50) NOT NULL,
    cost DOUBLE PRECISION NOT NULL
);
INSERT INTO operation (id, "type", cost) VALUES('2d0d70e5-3a83-11ef-8ba6-0242ac120002', 'ADDITION', 2.0);
INSERT INTO operation (id, "type", cost) VALUES('2d0eac37-3a83-11ef-8ba6-0242ac120002', 'SUBTRACTION', 3.0);
INSERT INTO operation (id, "type", cost) VALUES('2d0fdbfa-3a83-11ef-8ba6-0242ac120002', 'MULTIPLICATION', 4.0);
INSERT INTO operation (id, "type", cost) VALUES('2d10fff3-3a83-11ef-8ba6-0242ac120002', 'DIVISION', 5.0);
INSERT INTO operation (id, "type", cost) VALUES('2d121969-3a83-11ef-8ba6-0242ac120002', 'SQUARE_ROOT', 6.0);
INSERT INTO operation (id, "type", cost) VALUES('2d12ed7a-3a83-11ef-8ba6-0242ac120002', 'RANDOM_STRING', 20.0);

CREATE TABLE IF NOT EXISTS record_value (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    operation_id UUID,
    user_id UUID,
    amount DOUBLE PRECISION,
    user_balance DOUBLE PRECISION,
    operation_response VARCHAR(255),
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (operation_id) REFERENCES operation(id)
);


