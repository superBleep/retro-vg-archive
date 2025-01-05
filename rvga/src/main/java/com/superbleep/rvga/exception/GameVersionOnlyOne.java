package com.superbleep.rvga.exception;

import com.superbleep.rvga.exception.general.BadRequestException;
import com.superbleep.rvga.model.GameVersionId;

public class GameVersionOnlyOne extends BadRequestException {
    public GameVersionOnlyOne(GameVersionId id) {
        super(STR."Game version with id \{id.getId()} is the only version for game with id \{id.getGameId()}");
    }
}
