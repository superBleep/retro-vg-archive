package com.superbleep.nrvga.util;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessageSourceUtil {
    private static MessageSource messageSource;

    @Autowired
    private MessageSource injMessageSource;

    @PostConstruct
    public void init() {
        messageSource = injMessageSource;
    }

    public static String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, Locale.getDefault());
    }
}
