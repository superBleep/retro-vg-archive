package com.superbleep.nrvga.exception;

import com.superbleep.nrvga.util.MessageSourceUtil;

public class ArchiveUserSameUsername extends RuntimeException {
    public ArchiveUserSameUsername(String username) {
        super(MessageSourceUtil.getMessage("archive.user.same.username", username));
    }
}
