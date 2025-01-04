package com.superbleep.rvga.dto;

import com.superbleep.rvga.model.Game;

import java.util.Date;

public record GameVersionGet(String id, Game game, Date release, String notes) {
}
