package com.caiwei.object.entities.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Player {
    BLACK(0, "black"),

    WHITE(0, "white");

    private Integer code;
    private String player;

    public static Player getPlayer(String player) {
        return BLACK.getPlayer().equals(player) ? BLACK : WHITE;
    }
}
