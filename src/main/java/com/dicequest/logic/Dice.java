package com.dicequest.logic;

import java.util.Random;

public class Dice { // dedicate die class, so wont use multiple die function
    private static final Random rand = new Random();

    public static int roll(int sides) {
        return rand.nextInt(sides) + 1;
    }

    public static int roll2d6() { // rolls 2 6 sideed dice adds them for total
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