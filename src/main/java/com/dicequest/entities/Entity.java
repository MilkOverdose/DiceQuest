package com.dicequest.entities;

public abstract class Entity implements Combatant {
    protected String name;
    protected int hp;
    protected int maxHp;
    protected int baseDmg;

    public Entity(String name, int hp, int baseDmg) {
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

    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getBaseDmg() { return baseDmg; }
}