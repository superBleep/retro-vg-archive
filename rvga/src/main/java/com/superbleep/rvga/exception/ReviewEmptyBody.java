package com.superbleep.rvga.exception;

import com.superbleep.rvga.exception.general.BadRequestException;

public class ReviewEmptyBody extends BadRequestException {
    public ReviewEmptyBody() {
        super("Request must modify at least one field");
    }
}
