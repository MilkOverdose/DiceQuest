package com.dicequest.gui;

import com.dicequest.logic.*;
import com.dicequest.entities.*;
import javax.swing.*;
import java.awt.*;

public class GameDisplay implements GameView {
    public static final Color BG_DARK = new Color(18, 18, 24);
    public static final Color BG_PANEL = new Color(28, 28, 38);
    public static final Color GOLD = new Color(212, 175, 55);
    public static final Color GOLD_DIM = new Color(140, 110, 30);
    public static final Color HP_RED = new Color(180, 40, 40);
    public static final Color HP_GREEN = new Color(40, 180, 80);
    public static final Font TITLE_FONT = new Font("Georgia", Font.BOLD, 20);
    public static final Font LABEL_FONT = new Font("Georgia", Font.BOLD, 13);
    public static final Font LOG_FONT = new Font("Monospaced", Font.PLAIN, 12);

    private final JFrame frame;
    private final JLabel floorDisplay;
    private final JLabel enemyLabel;
    private final JProgressBar enemyHPBar;
    private final JProgressBar playerHPBar;
    private final JTextArea battleLog;
    private final JButton rollButton;
    private final GameController controller;

    public GameDisplay(GameGUI gui, GameController controller) {
        this.frame = gui.getFrame();
        this.floorDisplay = gui.getFloorDisplay();
        this.enemyLabel = gui.getEnemyLabel();
        this.enemyHPBar = gui.getEnemyHPBar();
        this.playerHPBar = gui.getPlayerHPBar();
        this.battleLog = gui.getBattleLog();
        this.rollButton = gui.getRollButton();
        this.controller = controller;

        // Wire up the roll button here not in GameGUI
        rollButton.addActionListener(e -> {
            rollButton.setEnabled(false);
            controller.processTurn();
            rollButton.setEnabled(!controller.getState().isGameOver());
        });
    }

    @Override
    public void updateDisplay(GameState state) {
        Player player = state.getPlayer();
        Enemy enemy = state.getCurrentEnemy();

        floorDisplay.setText("⚔ Floor " + state.getFloor() + " ⚔");
        enemyLabel.setText(enemy.getName());

        enemyHPBar.setMaximum(enemy.getMaxHp());
        enemyHPBar.setValue(enemy.getHp());
        enemyHPBar.setString("HP: " + enemy.getHp() + " / " + enemy.getMaxHp());

        playerHPBar.setMaximum(player.getMaxHp());
        playerHPBar.setValue(player.getHp());
        playerHPBar.setString("HP: " + player.getHp() + " / " + player.getMaxHp());
    }

    @Override
    public void updateBattleLog(String text) {
        battleLog.append(text);
        battleLog.setCaretPosition(battleLog.getDocument().getLength());
    }

    @Override
    public void showRewardScreen() {
        rollButton.setEnabled(false);
        String[] options = {"✚ Heal 30 HP", "⚔ Boost Damage +5", "→ Proceed"};
        int choice = JOptionPane.showOptionDialog(frame,
            "Victory! Choose your reward:",
            "Floor Cleared!",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null, options, options[0]);

        RewardType reward = switch (choice) {
            case 0 -> RewardType.HEAL;
            case 1 -> RewardType.DAMAGE_BOOST;
            default -> RewardType.NONE;
        };

        controller.applyReward(reward);
        rollButton.setEnabled(true);
    }

    @Override
    public void showGameOver() {
        rollButton.setEnabled(false);
        int choice = JOptionPane.showConfirmDialog(frame,
            "You have fallen on Floor " + controller.getState().getFloor() +
            ".\nRise again?",
            "Defeated",
            JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            controller.restart();
            rollButton.setEnabled(true);
        } else {
            System.exit(0);
        }
    }
}