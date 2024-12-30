package com.superbleep.rvga.exception;

import org.springframework.dao.DataAccessException;

public class GameIdenticalFound extends DataAccessException {
    public GameIdenticalFound() {
        super("There's already a game with the same title, released on the same platform.");
    }
}
