package com.dicequest.entities;

public class Enemy extends Entity {

    public Enemy(String name, int hp, int baseDmg) {
        super(name, hp, baseDmg);
    }

    public int attack() {
        return baseDmg;
    }
}