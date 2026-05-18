package com.dicequest.logic;

import java.util.Random;

public class Dice {
    private static final Random rand = new Random();

    public static int roll(int sides) {
        return rand.nextInt(sides) + 1;
    }

    public static int roll2d6() {
        return roll(6) + roll(6);
    }

    public static int rollNd(int numberOfDice, int sides) {
        int total = 0;
        for (int i = 0; i < numberOfDice; i++) {
            total += roll(sides);
        }
        return total;
    }
}