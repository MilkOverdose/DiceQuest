package com.dicequest.gui;

import com.dicequest.logic.SaveManager;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {

    public MainMenu() {
        super("Dice Quest");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 700);
        setResizable(false);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Theme.BG_DARK);
        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Theme.BG_DARK);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(80, 60, 80, 60));

        mainPanel.add(buildTitle());
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(buildSubtitle());
        mainPanel.add(Box.createVerticalStrut(60));
        mainPanel.add(Theme.buildDivider());
        mainPanel.add(Box.createVerticalStrut(40));
        mainPanel.add(buildStartButton());
        mainPanel.add(Box.createVerticalStrut(15));

        if (SaveManager.saveExists()) {
            mainPanel.add(buildContinueButton());
            mainPanel.add(Box.createVerticalStrut(15));
        }

        mainPanel.add(buildQuitButton());

        add(mainPanel);
    }

    private JLabel buildTitle() {
        JLabel title = new JLabel("DICE QUEST");
        title.setFont(new Font("Georgia", Font.BOLD, 42));
        title.setForeground(Theme.GOLD);
        title.setAlignmentX(CENTER_ALIGNMENT);
        return title;
    }

    private JLabel buildSubtitle() {
        JLabel subtitle = new JLabel("A Dungeon of Chance");
        subtitle.setFont(new Font("Georgia", Font.ITALIC, 16));
        subtitle.setForeground(Theme.GOLD_DIM);
        subtitle.setAlignmentX(CENTER_ALIGNMENT);
        return subtitle;
    }

    private MenuButton buildStartButton() {
        MenuButton startButton = new MenuButton("START GAME");
        startButton.addActionListener(e -> {
            dispose();
            new ClassSelection();
        });
        return startButton;
    }

    private MenuButton buildContinueButton() {
        MenuButton continueButton = new MenuButton("CONTINUE");
        continueButton.addActionListener(e -> {
            try {
                SaveManager.SaveData save = SaveManager.load();
                dispose();
                launchFromSave(save);
            } catch (SaveManager.SaveCorruptException ex) {
                JOptionPane.showMessageDialog(this,
                        "Save file is corrupt and cannot be loaded.\n" + ex.getMessage(),
                        "Load Failed",
                        JOptionPane.ERROR_MESSAGE);
                SaveManager.deleteSave();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Failed to load save: " + ex.getMessage(),
                        "Load Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        return continueButton;
    }

    private MenuButton buildQuitButton() {
        MenuButton quitButton = new MenuButton("QUIT");
        quitButton.addActionListener(e -> System.exit(0));
        return quitButton;
    }

    private void launchFromSave(SaveManager.SaveData save) {
        try {
            com.dicequest.logic.GameLogic gameLogic = new com.dicequest.logic.GameLogic(save);
            GameWindow window = new GameWindow();
            UIUpdater updater = new UIUpdater(window, gameLogic);
            gameLogic.setScreen(updater);
            updater.updateDisplay(gameLogic.getState());
            updater.updateBattleLog("Welcome back! Floor " + save.floor() + " — A "
                    + gameLogic.getState().getCurrentEnemy().getName() + " appears!\n");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Failed to launch game: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}