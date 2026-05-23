package com.dicequest.entities;

import com.dicequest.logic.Dice;

/**
 * Knight — a resilient warrior who builds Resolve Stacks through combat,
 * gaining damage reduction and unlocking powerful abilities.
 */
public class Knight extends Player {

    // ── Resolve ───────────────────────────────────────────────────────
    private static final int MAX_RESOLVE       = 5;
    private static final int LETHAL_SURVIVAL_HP = 10;

    // ── Parry & Riposte ───────────────────────────────────────────────
    private static final int    PARRY_SUCCESS_THRESHOLD   = 7;
    private static final double RIPOSTE_DAMAGE_MULTIPLIER = 0.5;

    // ── Pommel Strike ─────────────────────────────────────────────────
    private static final int    POMMEL_RESOLVE_COST      = 2;
    private static final double POMMEL_DAMAGE_MULTIPLIER = 1.3;
    private static final int    POMMEL_STUN_CHANCE       = 30;

    // ── Head Splitter ─────────────────────────────────────────────────
    private static final int    SPLITTER_HIT_THRESHOLD     = 7;
    private static final int    SPLITTER_CRIT_CHANCE       = 20;
    private static final double SPLITTER_CRIT_MULTIPLIER   = 2.5;
    private static final double SPLITTER_HIT_MULTIPLIER    = 1.8;
    private static final int    SPLITTER_MISS_PENALTY      = 2;
    private static final int    SPLITTER_SNAKE_EYES_PENALTY = 4;

    // ── Damage Reduction ──────────────────────────────────────────────
    private static final double DR_PER_POMMEL_STUN   = 0.05;
    private static final double MAX_DAMAGE_REDUCTION = 0.25;

    // ── State ─────────────────────────────────────────────────────────
    private int     resolveStacks  = 0;
    private double  damageReduction = 0.0;
    private boolean stunned        = false;
    private int     freeTurns      = 0;

    public Knight() {
        super("Knight", 150, 20, 30, 0.30);
    }

    // ─────────────────────────────────────────────────────────────────
    // COMBAT OVERRIDES
    // ─────────────────────────────────────────────────────────────────

    @Override
    public void takeDamage(int amount) {
        int reduced = (int)(amount * (1.0 - damageReduction));
        super.takeDamage(reduced);
    }

    @Override
    public int attack() {
        return getBaseDmg();
    }

    /**
     * Executes the Knight's chosen skill and returns the full turn result
     * including damage, log message, and combat flags for GameLogic to apply.
     */
    @Override
    public PlayerTurnResult executeTurn(String action) {
        return switch (action) {
            case "BASIC" -> {
                int dmg = attack();
                yield new PlayerTurnResult(dmg,
                    "You use Basic Attack! Hit for " + dmg + " damage.\n",
                    false, false);
            }
            case "PARRY" -> {
                RiposteResult res = parryAndRiposte();
                if (res.riposted()) {
                    yield new PlayerTurnResult(res.damage(),
                        "You adopt a Defensive Stance! Rolled: " + res.roll() + ".\n" +
                        "⚡ Riposte Success! Dealt " + res.damage() + " damage and earned +1 Resolve Stack.\n",
                        true, false);
                } else {
                    yield new PlayerTurnResult(0,
                        "You adopt a Defensive Stance! Rolled: " + res.roll() + ".\n" +
                        "❌ Parry failed! Unable to catch the target's rhythm.\n",
                        false, false);
                }
            }
            case "POMMEL" -> {
                PommelResult res = pommelStrike();
                if (!res.used()) yield new PlayerTurnResult(0, "", false, false);
                String log = "You execute Pommel Strike! Smashed enemy for " + res.damage() + " damage.\n";
                if (res.stunned()) {
                    setStunned(true);
                    log += "🎯 Enemy is STUNNED! Gained +5% Damage Reduction.\n";
                }
                yield new PlayerTurnResult(res.damage(), log, false, res.stunned());
            }
            case "SPLITTER" -> {
                HeadSplitterResult res = headSplitter();
                if (res.hit()) {
                    yield new PlayerTurnResult(res.damage(),
                        "You swing Head Splitter! Rolled: " + res.roll() + ".\n" +
                        "💥 Direct Hit! Cleaved down into target for " + res.damage() + " massive damage.\n",
                        false, false);
                } else {
                    yield new PlayerTurnResult(0,
                        "You swing Head Splitter! Rolled: " + res.roll() + ".\n" +
                        (res.isSnakeEyes()
                            ? "🚨 CRITICAL FUMBLE! Snake eyes! Penalized for 4 stasis turns.\n"
                            : "💨 Missed! Over-committed swing leaves you off balance for 2 turns.\n"),
                        false, false);
                }
            }
            default -> new PlayerTurnResult(0, "", false, false);
        };
    }

    // ─────────────────────────────────────────────────────────────────
    // SKILLS
    // ─────────────────────────────────────────────────────────────────

    public boolean attemptLethalSurvival(int damage) {
        if (resolveStacks >= MAX_RESOLVE && getHp() - damage <= 0) {
            resolveStacks = 0;
            setHp(LETHAL_SURVIVAL_HP);
            return true;
        }
        return false;
    }

    public RiposteResult parryAndRiposte() {
        int roll = Dice.roll2d6();
        boolean riposted = roll >= PARRY_SUCCESS_THRESHOLD;
        int damage = 0;
        if (riposted) {
            damage = (int)(getBaseDmg() * RIPOSTE_DAMAGE_MULTIPLIER);
            gainResolve(1);
        }
        return new RiposteResult(roll, riposted, damage);
    }

    public PommelResult pommelStrike() {
        if (resolveStacks < POMMEL_RESOLVE_COST) return new PommelResult(false, 0, false);
        resolveStacks -= POMMEL_RESOLVE_COST;
        int damage = (int)(getBaseDmg() * POMMEL_DAMAGE_MULTIPLIER);
        boolean stunned = Dice.roll(100) <= POMMEL_STUN_CHANCE;
        applyDamageReduction();
        return new PommelResult(true, damage, stunned);
    }

    public HeadSplitterResult headSplitter() {
        int roll = Dice.roll2d6();
        boolean isSnakeEyes    = roll == 2;
        boolean hit            = roll >= SPLITTER_HIT_THRESHOLD;
        boolean guaranteedCrit = resolveStacks >= MAX_RESOLVE;
        int damage             = 0;
        int penaltyTurns       = 0;

        if (hit) {
            boolean isCrit = guaranteedCrit || Dice.roll(100) <= SPLITTER_CRIT_CHANCE;
            damage = isCrit
                ? (int)(getBaseDmg() * SPLITTER_CRIT_MULTIPLIER)
                : (int)(getBaseDmg() * SPLITTER_HIT_MULTIPLIER);
        } else {
            penaltyTurns = isSnakeEyes ? SPLITTER_SNAKE_EYES_PENALTY : SPLITTER_MISS_PENALTY;
            freeTurns += penaltyTurns;
        }

        return new HeadSplitterResult(roll, hit, damage, penaltyTurns, isSnakeEyes);
    }

    // ─────────────────────────────────────────────────────────────────
    // STATE MANAGEMENT
    // ─────────────────────────────────────────────────────────────────

    public void gainResolve(int amount) {
        resolveStacks = Math.min(resolveStacks + amount, MAX_RESOLVE);
    }

    public void resetFloor() {
        resolveStacks   = 0;
        damageReduction = 0.0; // DR is battle-scoped; resets each floor
        freeTurns       = 0;
        stunned         = false;
    }

    private void applyDamageReduction() {
        damageReduction = Math.min(damageReduction + DR_PER_POMMEL_STUN, MAX_DAMAGE_REDUCTION);
    }

    // ─────────────────────────────────────────────────────────────────
    // GETTERS & SETTERS
    // ─────────────────────────────────────────────────────────────────

    public int     getResolveStacks()  { return resolveStacks;   }
    public double  getDamageReduction() { return damageReduction; }
    public boolean isStunned()         { return stunned;         }
    public int     getFreeTurns()      { return freeTurns;       }

    public void setStunned(boolean stunned) { this.stunned = stunned; }

    public void consumeFreeTurn() {
        if (freeTurns > 0) freeTurns--;
    }

    // ─────────────────────────────────────────────────────────────────
    // RESULT RECORDS
    // ─────────────────────────────────────────────────────────────────

    public record RiposteResult(int roll, boolean riposted, int damage) {}
    public record PommelResult(boolean used, int damage, boolean stunned) {}
    public record HeadSplitterResult(int roll, boolean hit, int damage,
                                     int penaltyTurns, boolean isSnakeEyes) {}
}