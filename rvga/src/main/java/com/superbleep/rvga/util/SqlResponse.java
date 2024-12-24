package com.superbleep.rvga.util;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(hidden = true)
public class SqlResponse extends MessageResponse {
    private final String sqlMessage;

    public SqlResponse(String message, String sqlMessage) {
        super(message);
        this.sqlMessage = sqlMessage;
    }

    public String getSqlMessage() {
        return sqlMessage;
    }
}
