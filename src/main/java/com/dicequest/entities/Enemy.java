package com.dicequest.entities;

public class Enemy extends Combatant {

    public Enemy(String name, int hp, int baseDmg) {
        super(name, hp, baseDmg);
    }

    @Override
    public int attack() {
        return getBaseDmg();
    }

    public EnemyTurnResult takeTurn() {
        return new EnemyTurnResult(attack(), 0, false);
    }

    public record EnemyTurnResult(int damage, int debuffDamage, boolean appliedDebuff) {
        public int totalDamage() {
            return damage + debuffDamage;
        }
    }
}