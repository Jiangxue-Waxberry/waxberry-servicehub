CREATE TABLE IF NOT EXISTS oauth2_authorization_consent (
     registered_client_id varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
     principal_name varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
     authorities varchar(1000) COLLATE utf8mb4_unicode_ci NOT NULL,
     PRIMARY KEY (registered_client_id,principal_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;