package com.dicequest.logic;

import com.dicequest.entities.*;

public class GameState {
    private int floor;
    private boolean gameOver;
    private boolean playerWon;
    private final Player player;
    private Enemy currentEnemy;

    public GameState(Player player) {
        this.floor = 1;
        this.gameOver = false;
        this.playerWon = false;
        this.player = player;
        this.currentEnemy = EnemyFactory.createForFloor(floor);
    }

    public void advanceFloor() {
        floor++;
        currentEnemy = EnemyFactory.createForFloor(floor);
    }

    public void markPlayerDefeated() {
        this.gameOver = true;
        this.playerWon = false;
    }


    public boolean isGameOver() { return gameOver; }
    public boolean isPlayerWon() { return playerWon; }
    public boolean isBossFloor() { return floor % 5 == 0; }
    public int getFloor() { return floor; }
    public Player getPlayer() { return player; }
    public Enemy getCurrentEnemy() { return currentEnemy; }
}