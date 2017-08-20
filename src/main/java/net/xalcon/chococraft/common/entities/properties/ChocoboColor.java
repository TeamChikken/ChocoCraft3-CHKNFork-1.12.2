package net.xalcon.chococraft.common.entities.properties;

import java.util.Random;

public enum ChocoboColor
{
    YELLOW,
    GREEN,
    BLUE,
    WHITE,
    BLACK,
    GOLD,
    PINK,
    RED,
    PURPLE;

    private static Random rand = new Random();

    public static ChocoboColor getRandomColor()
    {
        return values()[rand.nextInt(values().length)];
    }
}
