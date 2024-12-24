package com.superbleep.rvga.util;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Schema(hidden = true)
public record InvalidFieldsResponse(Map<String, String> invalidFields) {
}
