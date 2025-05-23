--liquibase formatted sql


--changeset jimmyD:1
CREATE TABLE IF NOT EXISTS users
(
    id          BIGSERIAL PRIMARY KEY,
    username    VARCHAR(20)  NOT NULL UNIQUE,
    password    VARCHAR(128) NOT NULL,
    email       VARCHAR(64)  NOT NULL UNIQUE,

    created_at  TIMESTAMP,
    modified_at TIMESTAMP,
    created_by  VARCHAR(64),
    modified_by VARCHAR(64)
);

--changeset jimmyD:2
CREATE TABLE IF NOT EXISTS notes
(
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(128),
    tag         VARCHAR(64),
    content     TEXT   NOT NULL,
    author      BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,

    created_at  TIMESTAMP,
    modified_at TIMESTAMP,
    created_by  VARCHAR(64),
    modified_by VARCHAR(64)
);

CREATE TABLE IF NOT EXISTS user_roles
(
    user_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
    role    VARCHAR(10),
    PRIMARY KEY (user_id, role)

)
