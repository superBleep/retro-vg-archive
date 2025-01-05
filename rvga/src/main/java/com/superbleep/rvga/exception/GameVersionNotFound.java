package com.superbleep.rvga.exception;

import com.superbleep.rvga.exception.general.NotFoundException;
import com.superbleep.rvga.model.GameVersionId;

public class GameVersionNotFound extends NotFoundException {
    public GameVersionNotFound(GameVersionId id) {
        super(STR."Game version with id \{id.getId()}, for game with id \{id.getGameId()}"
                + " doesn't exist in the database");
    }
}
