package com.dicequest.entities;

public class Enemy implements Combatant {
    private String name;
    private int hp;
    private int maxHp;
    private int baseDmg;

    public Enemy(String name, int hp, int baseDmg) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.baseDmg = baseDmg;
    }

    @Override
    public void takeDamage(int amount) {
        hp = Math.max(0, hp - amount);
    }

    @Override
    public boolean isAlive() {
        return hp > 0;
    }

    public int attack() {
        return baseDmg;
    }

    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
}