package com.superbleep.nrvga.exception;

import com.superbleep.nrvga.util.MessageSourceUtil;

public class ArchiveUserSameEmail extends RuntimeException {
    public ArchiveUserSameEmail(String email) {
        super(MessageSourceUtil.getMessage("archive.user.same.email", email));
    }
}
