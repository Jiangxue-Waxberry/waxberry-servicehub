package com.jiangxue.waxberry.user.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;

public class CustomJdbcRegisteredClientRepository extends JdbcRegisteredClientRepository {

    private static final String TABLE_NAME = "im_oauth2_registered_client";

    // @formatter:off
    private static final String COLUMN_NAMES = "id, "
            + "client_id, "
            + "client_id_issued_at, "
            + "client_secret, "
            + "client_secret_expires_at, "
            + "client_name, "
            + "client_authentication_methods, "
            + "authorization_grant_types, "
            + "redirect_uris, "
            + "post_logout_redirect_uris, "
            + "scopes, "
            + "client_settings,"
            + "token_settings";
    // @formatter:on

    private static final String PK_FILTER = "id = ?";

    private static final String LOAD_REGISTERED_CLIENT_SQL = "SELECT " + COLUMN_NAMES + " FROM " + TABLE_NAME
            + " WHERE ";

    // @formatter:off
    private static final String INSERT_REGISTERED_CLIENT_SQL = "INSERT INTO " + TABLE_NAME
            + "(" + COLUMN_NAMES + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    // @formatter:on

    // @formatter:off
    private static final String UPDATE_REGISTERED_CLIENT_SQL = "UPDATE " + TABLE_NAME
            + " SET client_secret = ?, client_secret_expires_at = ?, client_name = ?, client_authentication_methods = ?,"
            + " authorization_grant_types = ?, redirect_uris = ?, post_logout_redirect_uris = ?, scopes = ?,"
            + " client_settings = ?, token_settings = ?"
            + " WHERE " + PK_FILTER;
    // @formatter:on

    private static final String COUNT_REGISTERED_CLIENT_SQL = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE ";

    public CustomJdbcRegisteredClientRepository(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }
}
