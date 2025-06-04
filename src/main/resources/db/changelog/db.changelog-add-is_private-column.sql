--liquibase formatted sql


--changeset jimmyD:1
ALTER TABLE notes ADD is_private BOOLEAN NOT NULL DEFAULT FALSE