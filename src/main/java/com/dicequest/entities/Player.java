package com.dicequest.entities;

import com.dicequest.logic.Dice;

/**
 * Base player class. Holds core combat stats and shared actions available
 * to all player-controlled characters. Subclasses override attack() and
 * executeTurn() to implement class-specific combat behavior.
 */
public abstract class Player extends Combatant {

    private final int agility;
    private final double critMultiplier;

    public Player(String name, int hp, int baseDmg, int agility, double critMultiplier) {
        super(name, hp, baseDmg);
        this.agility = agility;
        this.critMultiplier = critMultiplier;
    }

    /**
     * Executes the player's chosen skill for this turn.
     *
     * @param action skill tag (e.g. "BASIC", "PARRY", "POMMEL", "SPLITTER")
     * @return result containing damage dealt, log message, and combat flags
     */
    public abstract PlayerTurnResult executeTurn(String action);

    /**
     * Rolls against agility to determine if this player dodges an attack.
     *
     * @return true if the dodge succeeds
     */
    public boolean attemptDodge() {
        return Dice.roll(100) < agility;
    }

    /**
     * Restores HP by the given amount, capped at max HP.
     *
     * @param amount HP to restore; ignored if zero or negative
     */
    public void heal(int amount) {
        if (amount <= 0) return;
        setHp(getHp() + amount);
    }

    /**
     * Permanently increases base damage by the given amount.
     *
     * @param amount damage to add; ignored if zero or negative
     */
    public void increaseDamage(int amount) {
        if (amount <= 0) return;
        setBaseDmg(getBaseDmg() + amount);
    }

    /**
     * Permanently increases maximum HP by the given amount.
     *
     * @param amount HP to add to the max; ignored if zero or negative
     */
    public void increaseMaxHp(int amount) {
        if (amount <= 0) return;
        setMaxHp(getMaxHp() + amount);
    }

    public int getAgility()          { return agility;        }
    public double getCritMultiplier() { return critMultiplier; }

    // ─────────────────────────────────────────────────────────────────
    // RESULT RECORD
    // ─────────────────────────────────────────────────────────────────

    /**
     * Carries the outcome of a player's turn back to GameLogic.
     *
     * @param damageDealt  total damage to apply to the enemy
     * @param logMessage   combat log text to display
     * @param parryNegated true if a successful parry should block the enemy counter
     * @param stunned      true if the enemy should be stunned this turn
     */
    public record PlayerTurnResult(
            int damageDealt,
            String logMessage,
            boolean parryNegated,
            boolean stunned) {}
}