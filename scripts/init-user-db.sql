CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE "user" (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    status BOOLEAN NOT NULL,
    balance DOUBLE PRECISION NOT NULL
);
INSERT INTO "user" (id, username, password, status, balance) VALUES('c2940f83-3a7d-11ef-be39-0242ac120002', 'user_1', '$2a$10$6l5gVGWMhJ.htaBTSjJO..wMHMQ5EYyZe0dnu893PwcBiUeLLluUO', TRUE, 10000.0);
INSERT INTO "user" (id, username, password, status, balance) VALUES('c2940f83-3a7d-11ef-be39-0242ac120003', 'user_2', '$2a$10$6l5gVGWMhJ.htaBTSjJO..wMHMQ5EYyZe0dnu893PwcBiUeLLluUO', TRUE, 10000.0);
