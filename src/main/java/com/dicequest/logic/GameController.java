package com.dicequest.logic;

import com.dicequest.entities.*;

public class GameController { //middleman between UI and Logic
    private GameState state; // basically tells what the UI should display without interacting with swing 
    private GameEngine engine;
    private GameView view;

    public GameController() {
        Player player = new Player();
        this.state = new GameState(player);
        this.engine = new GameEngine();
    }

    public void setView(GameView view) {
        this.view = view;
    }

    public void processTurn() {
        if (state.isGameOver()) return;

        Player player = state.getPlayer();
        Enemy enemy = state.getCurrentEnemy();

        CombatResult result = engine.processTurn(player, enemy);
        view.updateBattleLog(result.getSummary(enemy.getName()));
        view.updateDisplay(state);

        if (!enemy.isAlive()) {
            handleEnemyDefeated();
        } else if (!player.isAlive()) {
            handlePlayerDefeated();
        }
    }

    private void handleEnemyDefeated() {
        view.updateBattleLog("Enemy defeated!\n");
        view.showRewardScreen();
    }

    private void handlePlayerDefeated() {
        state.setPlayerDefeated();
        view.updateBattleLog("You have been defeated...\n");
        view.showGameOver();
    }

    public void applyReward(RewardType reward) {
        Player player = state.getPlayer();
        switch (reward) {
            case HEAL -> {
                player.heal(30);
                view.updateBattleLog("Healed 30 HP.\n");
            }
            case DAMAGE_BOOST -> {
                player.boostDamage(5);
                view.updateBattleLog("Damage boosted by 5!\n");
            }
            case NONE -> view.updateBattleLog("No reward taken.\n");
            default -> view.updateBattleLog("Unknown reward.\n");
        }
        advanceFloor();
    }

    public void advanceFloor() {
        engine.scaleDifficulty();
        state.advanceFloor();
        view.updateDisplay(state);
        view.updateBattleLog("\n--- Floor " + state.getFloor() +
            " ---\nA " + state.getCurrentEnemy().getName() + " appears!\n");
    }

    public void restart() {
        Player player = new Player();
        this.state = new GameState(player);
        this.engine = new GameEngine();
        view.updateDisplay(state);
        view.updateBattleLog("=== NEW GAME ===\nFloor 1 — A " +
            state.getCurrentEnemy().getName() + " appears!\n");
    }

    public GameState getState() { return state; }
}