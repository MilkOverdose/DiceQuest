package com.dicequest.entities;


import com.dicequest.logic.Dice;

public class Boss extends Enemy {
    private static final String BOSS_NAME = "BOSS";
    private static final int BOSS_HP = 150;
    private static final int DEBUFF_DAMAGE = 5;
    private int turnCount = 0;

    public Boss() {
        super(BOSS_NAME, BOSS_HP, 0);
    }

    @Override
    public int attack() {
        return rollBossDamage();
    }

   public int rollBossDamage() {
    return Dice.roll2d6();
    }   

    public boolean shouldApplyDebuff() {
        turnCount++;
        return turnCount % 2 == 0;
    }

    public int getDebuffDamage() {
        return DEBUFF_DAMAGE;
    }
}