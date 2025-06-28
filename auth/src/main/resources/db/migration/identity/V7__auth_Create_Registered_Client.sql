CREATE TABLE IF NOT EXISTS oauth2_registered_client (
       id varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
       client_id varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
       client_id_issued_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
       client_secret varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
       client_secret_expires_at timestamp NULL DEFAULT NULL,
       client_name varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
       client_authentication_methods varchar(1000) COLLATE utf8mb4_unicode_ci NOT NULL,
       authorization_grant_types varchar(1000) COLLATE utf8mb4_unicode_ci NOT NULL,
       redirect_uris varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
       post_logout_redirect_uris varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
       scopes varchar(1000) COLLATE utf8mb4_unicode_ci NOT NULL,
       client_settings varchar(2000) COLLATE utf8mb4_unicode_ci NOT NULL,
       token_settings varchar(2000) COLLATE utf8mb4_unicode_ci NOT NULL,
       PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;