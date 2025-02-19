package com.superbleep.nrvga.exception;

import com.superbleep.nrvga.util.MessageSourceUtil;

public class ArchiveUserSameUsernameAndEmail extends RuntimeException {
    public ArchiveUserSameUsernameAndEmail(String username, String email) {
        super(MessageSourceUtil.getMessage("archive.user.same.username.and.email", username, email));
    }
}
