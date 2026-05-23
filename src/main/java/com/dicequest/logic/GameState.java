package com.dicequest.logic;

import com.dicequest.entities.*;

public class GameState {

    private final Player player;
    private Enemy currentEnemy;
    private int     floor    = 1;
    private boolean gameOver = false;

    /** Fresh game starting at floor 1. */
    public GameState(Player player) {
        this.player       = player;
        this.currentEnemy = EnemyFactory.createForFloor(floor);
    }

    /** Restores game state from a save at a specific floor. */
    public GameState(Player player, int floor) {
        this.player       = player;
        this.floor        = floor;
        this.currentEnemy = EnemyFactory.createForFloor(floor);
    }

    public void advanceFloor() {
        floor++;
        currentEnemy = EnemyFactory.createForFloor(floor);
    }

    public void markPlayerDefeated() {
        this.gameOver = true;
    }

    public boolean isGameOver()      { return gameOver;      }
    public boolean isBossFloor()     { return floor % EnemyFactory.BOSS_FLOOR_INTERVAL == 0; }
    public int     getFloor()        { return floor;         }
    public Player  getPlayer()       { return player;        }
    public Enemy   getCurrentEnemy() { return currentEnemy;  }
}