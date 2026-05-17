package com.dicequest.logic;

public interface GameView {
    void updateDisplay(GameState state);
    void updateBattleLog(String text);
    void showRewardScreen();
    void showGameOver();
}