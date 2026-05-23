package com.dicequest.logic;

import com.dicequest.entities.*;
import com.dicequest.gui.UIUpdater;

/**
 * Core game loop. Processes player and enemy turns, manages floor progression,
 * and delegates UI updates to the registered UIUpdater screen.
 */
public class GameLogic {

    // ── Balance constants ─────────────────────────────────────────────
    private static final double ENEMY_DAMAGE_SCALE_PER_FLOOR = 0.15;
    private static final double BOSS_HP_BUFF_PERCENT = 0.20;
    private static final int BOSS_DAMAGE_BUFF = 5;
    private static final int REWARD_HEAL_AMOUNT = 30;
    private static final int REWARD_DAMAGE_AMOUNT = 5;

    // ── State ─────────────────────────────────────────────────────────
    private GameState session;
    private double enemyDamageModifier = 1.0; // scales up by ENEMY_DAMAGE_SCALE_PER_FLOOR each floor
    private UIUpdater screen;

    // ─────────────────────────────────────────────────────────────────
    // CONSTRUCTORS
    // ─────────────────────────────────────────────────────────────────

    /** Starts a fresh game with a new Knight. */
    public GameLogic() {
        this.session = new GameState(new Knight());
    }

    /**
     * Restores a game from a save file.
     *
     * @param save loaded save data
     */
    public GameLogic(SaveManager.SaveData save) {
        Knight knight = new Knight();

        // Restore max HP first before anything else
        int maxHpDiff = save.maxHp() - knight.getMaxHp();
        if (maxHpDiff > 0)
            knight.increaseMaxHp(maxHpDiff);

        // Set HP directly by taking damage from max
        int hpDiff = knight.getMaxHp() - save.hp();
        if (hpDiff > 0)
            knight.takeDamage(hpDiff);

        // Restore base damage
        int dmgDiff = save.baseDmg() - knight.getBaseDmg();
        if (dmgDiff > 0)
            knight.increaseDamage(dmgDiff);

        // Restore resolve stacks
        for (int i = 0; i < save.resolveStacks(); i++)
            knight.gainResolve(1);

        this.session = new GameState(knight, save.floor());
        this.enemyDamageModifier = save.enemyDamageModifier();
    }

    public void setScreen(UIUpdater screen) {
        this.screen = screen;
    }

    public GameState getState() {
        return session;
    }

    public double getEnemyDamageModifier() {
        return enemyDamageModifier;
    }

    // ─────────────────────────────────────────────────────────────────
    // TURN PROCESSING
    // ─────────────────────────────────────────────────────────────────

    public void processTurn(String action) {
        if (session.isGameOver())
            return;

        Knight knight = (Knight) session.getPlayer();
        Enemy enemy = session.getCurrentEnemy();

        // ── stasis penalty ────────────────────────────────────────────
        if (knight.getFreeTurns() > 0) {
            screen.updateBattleLog("You are penalized from missing Head Splitter! Skipping turn...\n");
            knight.consumeFreeTurn();
            executeEnemyTurn(knight, enemy, false);
            return;
        }

        // ── player action ─────────────────────────────────────────────
        Player.PlayerTurnResult result = knight.executeTurn(action);
        enemy.takeDamage(result.damageDealt());
        screen.updateBattleLog(result.logMessage());
        screen.updateDisplay(session);

        if (!enemy.isAlive()) {
            onEnemyDefeated();
            return;
        }

        // ── enemy turn ────────────────────────────────────────────────
        executeEnemyTurn(knight, enemy, result.parryNegated());
    }

    // ─────────────────────────────────────────────────────────────────
    // ENEMY TURN
    // ─────────────────────────────────────────────────────────────────

    private void executeEnemyTurn(Knight knight, Enemy enemy, boolean parryNegated) {
        if (!enemy.isAlive())
            return;

        if (parryNegated) {
            screen.updateBattleLog("🛡️ Perfect Defense! You completely deflected "
                    + enemy.getName() + "'s counter-attack!\n");
            screen.updateDisplay(session);
            return;
        }

        if (knight.isStunned()) {
            knight.setStunned(false);
            screen.updateBattleLog(enemy.getName() + " is reeling from stun and skips their turn!\n");
            screen.updateDisplay(session);
            return;
        }

        if (knight.attemptDodge()) {
            screen.updateBattleLog(enemy.getName() + " swung but you managed to dodge!\n");
        } else {
            Enemy.EnemyTurnResult result = enemy.takeTurn();
            int finalDmg = (int) (result.totalDamage() * enemyDamageModifier);

            if (knight.attemptLethalSurvival(finalDmg)) {
                screen.updateBattleLog(
                        "🛡 LETHAL SURVIVAL ACTIVE! Resolve stacks consumed to hold onto life at 10 HP!\n");
            } else {
                knight.takeDamage(finalDmg);
                screen.updateBattleLog(enemy.getName() + " hits you for " + finalDmg + " damage.\n");
            }

            if (result.appliedDebuff()) {
                screen.updateBattleLog(enemy.getName()
                        + " inflicts a necrotic curse! You suffer " + result.debuffDamage() + " damage.\n");
            }
        }

        screen.updateDisplay(session);

        if (!knight.isAlive()) {
            onPlayerDefeated();
        }
    }

    // ─────────────────────────────────────────────────────────────────
    // GAME EVENTS
    // ─────────────────────────────────────────────────────────────────

    private void onEnemyDefeated() {
        screen.updateBattleLog("Enemy defeated!\n");
        applyBossVictoryReward();
        screen.showRewardScreen();
    }

    private void applyBossVictoryReward() {
        if (session.getCurrentEnemy() instanceof Boss) {
            Knight knight = (Knight) session.getPlayer();
            int healthBuff = (int) (knight.getMaxHp() * BOSS_HP_BUFF_PERCENT);
            knight.increaseMaxHp(healthBuff);
            knight.heal(knight.getMaxHp());
            knight.increaseDamage(BOSS_DAMAGE_BUFF);
            screen.updateBattleLog("🏆 BOSS SLAIN! Max HP permanently increased by 20% to: "
                    + knight.getMaxHp() + "\n");
            screen.updateBattleLog("⚔ Base damage permanently increased by " + BOSS_DAMAGE_BUFF + "!\n");
        }
    }

    private void onPlayerDefeated() {
        session.markPlayerDefeated();
        screen.updateBattleLog("You have been defeated...\n");
        screen.showGameOver();
    }

    // ─────────────────────────────────────────────────────────────────
    // REWARDS & PROGRESSION
    // ─────────────────────────────────────────────────────────────────

    public void applyReward(RewardType reward) {
        Knight knight = (Knight) session.getPlayer();
        switch (reward) {
            case HEAL -> {
                knight.heal(REWARD_HEAL_AMOUNT);
                screen.updateBattleLog("Healed " + REWARD_HEAL_AMOUNT + " HP.\n");
            }
            case DAMAGE_BOOST -> {
                knight.increaseDamage(REWARD_DAMAGE_AMOUNT);
                screen.updateBattleLog("Damage boosted by " + REWARD_DAMAGE_AMOUNT + "!\n");
            }
            case NONE -> screen.updateBattleLog("No reward taken.\n");
        }
        advanceFloor();
    }

    public void advanceFloor() {
        enemyDamageModifier += ENEMY_DAMAGE_SCALE_PER_FLOOR;
        session.advanceFloor();
        ((Knight) session.getPlayer()).resetFloor();
        screen.updateDisplay(session);
        screen.updateBattleLog("--- Floor " + session.getFloor() + " ---\nA "
                + session.getCurrentEnemy().getName() + " appears!\n");

        // Auto-save between floors
        try {
            SaveManager.save(session, enemyDamageModifier);
        } catch (java.io.IOException ex) {
            screen.updateBattleLog("⚠ Auto-save failed: " + ex.getMessage() + "\n");
        }
    }

    public void restart() {
        this.session = new GameState(new Knight());
        this.enemyDamageModifier = 1.0;
        screen.updateDisplay(session);
        screen.updateBattleLog("=== NEW GAME ===\nFloor 1 — A "
                + session.getCurrentEnemy().getName() + " appears!\n");
    }
}