package com.superbleep.rvga.exception;

import com.superbleep.rvga.exception.general.NotFoundException;

public class ReviewNotFound extends NotFoundException {
    public ReviewNotFound(long id) {
        super(STR."Review with id \{id} doesn't exist in the database");
    }
}
