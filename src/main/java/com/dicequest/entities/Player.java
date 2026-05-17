package com.dicequest.entities;

public class Player implements Combatant {
    private int hp;
    private int maxHp;
    private int baseDmg;
    private int agility;
    private double critMultiplier;

    public Player() {
        this.hp = 150;           // up from 100
        this.maxHp = 150;        // up from 100
        this.baseDmg = 30;       // up from 20
        this.agility = 20;       // up from 10 — dodge more often
        this.critMultiplier = 0.35; // up from 0.30 — crit more often
    }

    @Override
    public void takeDamage(int amount) {
        hp = Math.max(0, hp - amount);
    }

    @Override
    public boolean isAlive() {
        return hp > 0;
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

    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getBaseDmg() { return baseDmg; }
    public double getCritMultiplier() { return critMultiplier; }
}