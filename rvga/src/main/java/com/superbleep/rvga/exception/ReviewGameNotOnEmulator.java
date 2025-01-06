package com.superbleep.rvga.exception;

import com.superbleep.rvga.exception.general.BadRequestException;

public class ReviewGameNotOnEmulator extends BadRequestException {
    public ReviewGameNotOnEmulator(long emulatorId, long gameId) {
        super(STR."Game with id \{gameId} has a platform not available on emulator with id \{emulatorId}");
    }
}
