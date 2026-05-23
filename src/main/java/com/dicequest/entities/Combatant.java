package com.dicequest.entities;

public abstract class Combatant {
    private final String name;
    private int hp;
    private int maxHp;
    private int baseDmg;

    public Combatant(String name, int hp, int baseDmg) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.baseDmg = baseDmg;
    }

    public abstract int attack();


    public void takeDamage(int amount) {
        if (amount < 0)
            return;
        hp = Math.max(0, hp - amount);
    }


    public boolean isAlive() {
        return hp > 0;
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getBaseDmg() {
        return baseDmg;
    }

    protected void setHp(int hp) {
        this.hp = Math.max(0, Math.min(hp, maxHp));
    }

    protected void setBaseDmg(int baseDmg) {
        this.baseDmg = Math.max(0, baseDmg);
    }

    protected void setMaxHp(int maxHp) {
        this.maxHp = Math.max(1, maxHp);
    }
}