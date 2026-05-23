package com.dicequest.logic;

import com.dicequest.entities.*;
import com.dicequest.gui.UIUpdater;

/**
 * Core game loop. Processes player and enemy turns, manages floor progression,
 * and delegates UI updates to the registered UIUpdater screen.
 */
public class GameLogic {

    private GameState session;
    private double enemyDamageModifier = 1.0;
    private UIUpdater screen;

    public GameLogic(Player player) {
        this.session = new GameState(new Knight());
    }

    public void setScreen(UIUpdater screen) { this.screen = screen; }
    public GameState getState()             { return session;        }

    // ─────────────────────────────────────────────────────────────────
    // TURN PROCESSING
    // ─────────────────────────────────────────────────────────────────

    public void processTurn(String action) {
        if (session.isGameOver()) return;

        Knight knight = (Knight) session.getPlayer();
        Enemy  enemy  = session.getCurrentEnemy();

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
        if (!enemy.isAlive()) return;

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
            int finalDmg = (int)(result.totalDamage() * enemyDamageModifier);

            if (knight.attemptLethalSurvival(finalDmg)) {
                screen.updateBattleLog("🛡 LETHAL SURVIVAL ACTIVE! Resolve stacks consumed to hold onto life at 10 HP!\n");
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
            Knight knight  = (Knight) session.getPlayer();
            int healthBuff = (int)(knight.getMaxHp() * 0.20);
            knight.increaseMaxHp(healthBuff);
            knight.heal(knight.getMaxHp());
            knight.increaseDamage(5);
            screen.updateBattleLog("🏆 BOSS SLAIN! Max HP permanently increased by 20% to: "
                    + knight.getMaxHp() + "\n");
            screen.updateBattleLog("⚔ Base damage permanently increased by 5!\n");
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
            case HEAL         -> { knight.heal(30);          screen.updateBattleLog("Healed 30 HP.\n"); }
            case DAMAGE_BOOST -> { knight.increaseDamage(5); screen.updateBattleLog("Damage boosted by 5!\n"); }
            case NONE         ->                             screen.updateBattleLog("No reward taken.\n");
        }
        advanceFloor();
    }

    public void advanceFloor() {
        enemyDamageModifier += 0.15;
        session.advanceFloor();
        ((Knight) session.getPlayer()).resetFloor();
        screen.updateDisplay(session);
        screen.updateBattleLog("--- Floor " + session.getFloor() + " ---\nA "
                + session.getCurrentEnemy().getName() + " appears!\n");
    }

    public void restart() {
        this.session = new GameState(new Knight());
        this.enemyDamageModifier = 1.0;
        screen.updateDisplay(session);
        screen.updateBattleLog("=== NEW GAME ===\nFloor 1 — A "
                + session.getCurrentEnemy().getName() + " appears!\n");
    }
}