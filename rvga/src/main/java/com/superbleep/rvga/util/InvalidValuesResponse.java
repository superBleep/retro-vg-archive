package com.superbleep.rvga.util;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(hidden = true)
public class InvalidValuesResponse extends MessageResponse {
    private final String causeMessage;

    public InvalidValuesResponse(String message, String causeMessage) {
        super(message);
        this.causeMessage = causeMessage;
    }

    public String getCauseMessage() {
        return causeMessage;
    }
}