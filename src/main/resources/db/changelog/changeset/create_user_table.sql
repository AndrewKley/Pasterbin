-- liquibase formatted sql

-- changeset liquibase:1


CREATE EXTENSION citext;
CREATE DOMAIN email AS citext
    CHECK (VALUE ~ '^[a-zA-Z0-9.!#$%&''*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$');

CREATE TABLE user
(
    id                  BIGINT CONSTRAINT pr_user PRIMARY KEY,
    first_name          VARCHAR(255) NOT NULL,
    last_name           VARCHAR(255) NOT NULL,
    email               email NOT NULL
);

COMMENT ON TABLE user IS 'User table';

-- rollback drop table user; drop extension citext cascade;