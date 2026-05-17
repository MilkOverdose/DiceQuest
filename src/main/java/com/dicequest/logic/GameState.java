package com.dicequest.logic;

import com.dicequest.entities.*;

public class GameState {
    private int floor;
    private boolean gameOver;
    private boolean playerWon;
    private Player player;
    private Enemy currentEnemy;

    public GameState(Player player) {
        this.floor = 1;
        this.gameOver = false;
        this.playerWon = false;
        this.player = player;
        this.currentEnemy = spawnEnemy();
    }

    private Enemy spawnEnemy() {
        if (floor % 5 == 0) {
            return new Boss();
        }
        return new Enemy("Goblin", 60 + (floor * 5), 8 + (floor * 2));
    }

    public void advanceFloor() {
        floor++;
        currentEnemy = spawnEnemy();
    }

    public void setPlayerDefeated() {
        this.gameOver = true;
        this.playerWon = false;
    }

    public void setPlayerVictorious() {
        this.gameOver = false;
        this.playerWon = false;
    }

    public boolean isGameOver() { return gameOver; }
    public boolean isPlayerWon() { return playerWon; }
    public boolean isBossFloor() { return floor % 5 == 0; }
    public int getFloor() { return floor; }
    public Player getPlayer() { return player; }
    public Enemy getCurrentEnemy() { return currentEnemy; }
}