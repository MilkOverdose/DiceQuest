package com.dicequest.entities;

import com.dicequest.logic.Dice;

public class Boss extends Enemy {
    private static final String BOSS_NAME = "Boss";
    private static final int BOSS_HP = 150;
    private static final int DEBUFF_DAMAGE = 5;
    private int turnCount = 0;

    public Boss() {
        super(BOSS_NAME, BOSS_HP, 0);
    }

    @Override
    public int attack() {
        return Dice.roll2d6();
    }

    @Override
    public EnemyTurnResult takeTurn() {
        turnCount++;
        int damage = attack();
        boolean debuffed = turnCount % 2 == 0;
        int debuffDamage = debuffed ? DEBUFF_DAMAGE : 0;
        return new EnemyTurnResult(damage, debuffDamage, debuffed);
    }
}