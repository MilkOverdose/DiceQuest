package com.dicequest.entities;

public class Player extends Entity { // is the player
    private int agility;
    private double critMultiplier;

    public Player() {
        super("Player", 150, 30);
        this.agility = 20;
        this.critMultiplier = 0.35;
    }

    public boolean checkDodge() { // if less than agility dodge, aligity is the chance off dodge
        return Math.random() * 100 < agility;
    }

    public void heal(int amount) { //adds HP to the max
        hp = Math.min(hp + amount, maxHp);
    }

    public void boostDamage(int amount) { // damage boost
        baseDmg += amount;
    }

    public double getCritMultiplier() { return critMultiplier; }
}