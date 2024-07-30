CREATE DATABASE db_example
    WITH
    OWNER = root
    ENCODING = 'UTF8'
    TABLESPACE = pg_default;

CREATE SCHEMA IF NOT EXISTS sc_example AUTHORIZATION root;

SET search_path TO sc_example;

CREATE TABLE geocodes (
           city varchar(255) PRIMARY KEY NOT NULL,
           lat DOUBLE NOT NULL,
           lon DOUBLE NOT NULL
);

CREATE TABLE pollution_info (
            city varchar(255) NOT NULL,
            date DATE NOT NULL,
            co DOUBLE NOT NULL,
            o3 DOUBLE NOT NULL,
            so2 DOUBLE NOT NULL,
            PRIMARY KEY (city, date)
);

