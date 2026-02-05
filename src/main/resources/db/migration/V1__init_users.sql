CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,

    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE,

    registration_date_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role VARCHAR(50) NOT NULL,
    CONSTRAINT fk_user_roles_users
        FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE CASCADE
);