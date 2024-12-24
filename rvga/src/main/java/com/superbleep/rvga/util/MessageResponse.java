package com.superbleep.rvga.util;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(hidden = true)
public class MessageResponse {
    private final String message;

    public MessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
