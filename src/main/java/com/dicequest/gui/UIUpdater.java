package com.dicequest.gui;

import com.dicequest.entities.*;
import com.dicequest.logic.*;
import javax.swing.*;
import com.dicequest.logic.SaveManager;

public class UIUpdater {
    private final GameWindow window;
    private final GameLogic gameLogic;
    private StringBuilder turnLog = new StringBuilder();

    public UIUpdater(GameWindow window, GameLogic gameLogic) {
        this.window = window;
        this.gameLogic = gameLogic;

        window.getRollButton().addActionListener(e -> {
            String activeSelection = window.getSelectedActionTag();

            if (activeSelection.equals("POMMEL") && gameLogic.getState().getPlayer() instanceof Knight knight) {
                if (knight.getResolveStacks() < 2) {
                    updateBattleLog("[System]: Not enough Resolve! Pommel Strike requires 2 stacks.\n");
                    return;
                }
            }

            processTurn(activeSelection);
        });

        

        updateDisplay(gameLogic.getState());

    }

    private void processTurn(String actionType) {
        window.setControlsEnabled(false);
        turnLog.setLength(0);
        gameLogic.processTurn(actionType);
        window.setControlsEnabled(!gameLogic.getState().isGameOver());
    }

    public void updateDisplay(GameState state) {
        Player player = state.getPlayer();
        Enemy enemy = state.getCurrentEnemy();

        window.getFloorDisplay().setText(" Floor " + state.getFloor());
        window.getEnemyLabel().setText(enemy.getName());
        window.getPlayerLabel().setText(player.getName());

        window.getEnemyHPBar().setMaximum(enemy.getMaxHp());
        window.getEnemyHPBar().setValue(enemy.getHp());
        window.getEnemyHPBar().setString("HP: " + enemy.getHp() + " / " + enemy.getMaxHp());

        window.getPlayerHPBar().setMaximum(player.getMaxHp());
        window.getPlayerHPBar().setValue(player.getHp());
        window.getPlayerHPBar().setString("HP: " + player.getHp() + " / " + player.getMaxHp());

        if (player instanceof Knight knight) {
            window.getLblResolveStacks().setText(String.format(
                    "RESOLVE STACKS: %d/%d  (DR: %.0f%%)",
                    knight.getResolveStacks(), 5, knight.getDamageReduction() * 100));
        }
    }

    public void updateBattleLog(String text) {
        turnLog.append(text);
        window.getBattleLog().setText(turnLog.toString());
        window.getBattleLog().setCaretPosition(0);
    }

    public void showRewardScreen() {
        window.setControlsEnabled(false);
        String[] options = { "✚ Heal 30 HP", "⚔ Boost Damage +5", "→ Proceed" };
        int choice = JOptionPane.showOptionDialog(window,
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

        gameLogic.applyReward(reward);

        SwingUtilities.invokeLater(() -> {
            updateDisplay(gameLogic.getState());
            turnLog.setLength(0);
            window.setControlsEnabled(true);
        });
    }

    public void showGameOver() {
        window.setControlsEnabled(false);
        int choice = JOptionPane.showConfirmDialog(window,
                "You have fallen on Floor " + gameLogic.getState().getFloor() + ".\nRise again?",
                "Defeated",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            SaveManager.deleteSave();
            gameLogic.restart();
            window.setControlsEnabled(true);
        } else {
            System.exit(0);
        }
    }
}