package com.dicequest.entities;

import java.util.Random;

public class Boss extends Enemy {
    private static final String BOSS_NAME = "BOSS";
    private static final int BOSS_HP = 150;
    private static final int DEBUFF_DAMAGE = 5;
    private Random rand = new Random();
    private int turnCount = 0;

    public Boss() {
        super(BOSS_NAME, BOSS_HP, 0);
    }

    @Override
    public int attack() {
        return rollBossDamage();
    }

    public int rollBossDamage() {
        return (rand.nextInt(6) + 1) + (rand.nextInt(6) + 1);
    }

    public boolean shouldApplyDebuff() {
        turnCount++;
        return turnCount % 2 == 0; // debuff every other turn
    }

    public int getDebuffDamage() {
        return DEBUFF_DAMAGE;
    }
}