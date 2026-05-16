
package com.dicequest;
public class Enemy implements Combatant {
    private int hp;
    private int baseDmg;
    private String name;

    public Enemy(String name, int hp, int baseDmg) {
        this.name = name;
        this.hp = hp;
        this.baseDmg = baseDmg;
    }

    @Override
    public void takeDamage(int amount) {
        hp -= amount;
    }

    @Override
    public boolean isAlive() {
        return hp > 0;
    }

    public int attack() { return baseDmg; }
    public int getHp() { return hp; }
    public String getName() { return name; }
}