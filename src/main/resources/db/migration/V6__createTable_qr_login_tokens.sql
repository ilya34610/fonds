CREATE TABLE qr_login_tokens (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(64) NOT NULL UNIQUE,
    id_user BIGINT NOT NULL,
    created_at TIMESTAMP  NOT NULL,
    expires_at TIMESTAMP  NOT NULL,
    used BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT qr_login_tokens_users_fk FOREIGN KEY (id_user) REFERENCES public.users(id) ON DELETE CASCADE
);