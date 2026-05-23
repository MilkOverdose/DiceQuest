package com.dicequest.logic;

import java.util.Random;

public final class Dice {
    private static final Random RAND = new Random();

    private Dice() {}

    public static int roll(int sides) {
        if (sides < 1) throw new IllegalArgumentException("Sides must be at least 1");
        return RAND.nextInt(sides) + 1;
    }

    public static int roll2d6() {
        return rollNd(2, 6);
    }

    public static int rollNd(int numberOfDice, int sides) {
        if (numberOfDice < 1) throw new IllegalArgumentException("Must roll at least 1 die");
        int total = 0;
        for (int i = 0; i < numberOfDice; i++) {
            total += roll(sides);
        }
        return total;
    }
}