package com.dicequest.entities;

public interface Combatant {
    void takeDamage(int amount);
    boolean isAlive();
}