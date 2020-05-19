package com.jon.cotbeacon.enums;

import android.content.SharedPreferences;

import com.jon.cotbeacon.utils.Key;
import com.jon.cotbeacon.utils.PrefUtils;

import java.util.Random;

public enum TeamColour {
    PURPLE("Purple"),
    MAGENTA("Magenta"),
    MAROON("Maroon"),
    RED("Red"),
    ORANGE("Orange"),
    YELLOW("Yellow"),
    WHITE("White"),
    GREEN("Green"),
    DARK_GREEN("Dark Green"),
    CYAN("Cyan"),
    TEAL("Teal"),
    BLUE("Blue"),
    DARK_BLUE("Dark Blue");

    final private String team;

    TeamColour(String colour) { this.team = colour; }

    public String get() { return team; }

    public static TeamColour fromPrefs(final SharedPreferences prefs) {
        String choice = Integer.toHexString(PrefUtils.getInt(prefs, Key.TEAM_COLOUR)).toUpperCase();
        switch (choice) {
            case "FF800080": return PURPLE;
            case "FFFF00FF": return MAGENTA;
            case "FF800000": return MAROON;
            case "FFFF0000": return RED;
            case "FFFF8000": return ORANGE;
            case "FFFFFF00": return YELLOW;
            case "FFFFFFFF": return WHITE;
            case "FF00FF00": return GREEN;
            case "FF006400": return DARK_GREEN;
            case "FF00FFFF": return CYAN;
            case "FF008080": return TEAL;
            case "FF0000FF": return BLUE;
            case "FF00008B": return DARK_BLUE;
            default: throw new IllegalArgumentException("Unknown team colour: " + choice);
        }
    }
}