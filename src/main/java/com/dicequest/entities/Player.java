package com.dicequest.entities;

public class Player extends Entity {
    private int agility;
    private double critMultiplier;

    public Player() {
        super("Player", 150, 30);
        this.agility = 20;
        this.critMultiplier = 0.35;
    }

    public boolean checkDodge() {
        return Math.random() * 100 < agility;
    }

    public void heal(int amount) {
        hp = Math.min(hp + amount, maxHp);
    }

    public void boostDamage(int amount) {
        baseDmg += amount;
    }

    public double getCritMultiplier() { return critMultiplier; }
}