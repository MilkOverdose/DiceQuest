package com.dicequest;
public class Player implements Combatant {
    private int hp = 100;
    private int maxHp = 100;
    private int baseDmg = 20;
    private int agility = 10;
    private double critMultiplier = 0.30;

    @Override
    public void takeDamage(int amount) {
        hp -= amount;
    }

    @Override
    public boolean isAlive() {
        return hp > 0;
    }

    public boolean checkDodge() {
        return Math.random() * 100 < agility;
    }

    public void applyReward(int hpGain) {
        hp = Math.min(hp + hpGain, maxHp);
    }

    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getBaseDmg() { return baseDmg; }
    public double getCritMultiplier() { return critMultiplier; }
}