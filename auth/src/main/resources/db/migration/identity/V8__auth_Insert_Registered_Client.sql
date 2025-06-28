INSERT INTO wb.oauth2_registered_client (id, client_id, client_id_issued_at, client_secret, client_secret_expires_at,
                                         client_name, client_authentication_methods, authorization_grant_types,
                                         redirect_uris, post_logout_redirect_uris, scopes, client_settings,
                                         token_settings)
VALUES ('e5ee87e7-0ea7-4720-b75b-17ed5e5423fd', 'spa-client', '2025-06-11 09:47:53', null, null,
        'e5ee87e7-0ea7-4720-b75b-17ed5e5423fd', 'none', 'refresh_token,authorization_code',
        'http://localhost/callback,http://127.0.0.1:3000/callback,http://localhost:3000/callback,http://localhost:80/callback,http://localhost:3000/callback,http://localhost/callback,http://localhost/callback,https://localhost/callback,https://localhost/callback',
        'http://localhost:3000,http://localhost:80/,http://localhost/logout,http://localhost/,http://localhost:3000/,http://127.0.0.1:3000/,http://localhost/logout,http://localhost/logout,https://localhost/logout,https://localhost/logout',
        'read,openid,profile,write,email,api.write,api.read',
        '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":true,"settings.client.require-authorization-consent":false}',
        '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.reuse-refresh-tokens":true,"settings.token.x509-certificate-bound-access-tokens":false,"settings.token.id-token-signature-algorithm":["org.springframework.security.oauth2.jose.jws.SignatureAlgorithm","RS256"],"settings.token.access-token-time-to-live":["java.time.Duration",2592000.000000000],"settings.token.access-token-format":{"@class":"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat","value":"self-contained"},"settings.token.refresh-token-time-to-live":["java.time.Duration",2592000.000000000],"settings.token.authorization-code-time-to-live":["java.time.Duration",300.000000000],"settings.token.device-code-time-to-live":["java.time.Duration",300.000000000]}');
commit;