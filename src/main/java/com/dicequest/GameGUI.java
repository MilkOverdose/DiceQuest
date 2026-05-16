package com.dicequest;
import javax.swing.*;
import java.awt.*;

public class GameGUI {
    private JButton rollButton;
    private JProgressBar playerHPBar;
    private JProgressBar enemyHPBar;
    private JLabel floorDisplay;
    private JLabel enemyLabel;
    private JTextArea battleLog;
    private JFrame frame;

    private GameEngine engine;
    private Player player;
    private Enemy currentEnemy;

    public GameGUI(GameEngine engine, Player player) {
        this.engine = engine;
        this.player = player;
        this.currentEnemy = spawnEnemy();
        buildUI();
    }

    private Enemy spawnEnemy() {
        if (engine.isBossFloor()) {
            return new Boss();
        }
        int floor = engine.getFloorCount();
        return new Enemy("Goblin", 60 + (floor * 5), 8 + (floor * 2));
    }

    private void buildUI() {
        frame = new JFrame("Dice Quest");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 550);
        frame.setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new FlowLayout());
        floorDisplay = new JLabel("Floor 1");
        floorDisplay.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(floorDisplay);

        JPanel statsPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        enemyLabel = new JLabel("Enemy: Goblin");
        enemyLabel.setFont(new Font("Arial", Font.BOLD, 14));

        enemyHPBar = new JProgressBar(0, currentEnemy.getHp());
        enemyHPBar.setValue(currentEnemy.getHp());
        enemyHPBar.setStringPainted(true);
        enemyHPBar.setString("Enemy HP: " + currentEnemy.getHp());
        enemyHPBar.setForeground(Color.RED);

        JLabel playerLabel = new JLabel("Player HP");
        playerLabel.setFont(new Font("Arial", Font.BOLD, 14));

        playerHPBar = new JProgressBar(0, player.getMaxHp());
        playerHPBar.setValue(player.getHp());
        playerHPBar.setStringPainted(true);
        playerHPBar.setString("Player HP: " + player.getHp());
        playerHPBar.setForeground(Color.GREEN);

        statsPanel.add(enemyLabel);
        statsPanel.add(enemyHPBar);
        statsPanel.add(playerLabel);
        statsPanel.add(playerHPBar);

        battleLog = new JTextArea(10, 30);
        battleLog.setEditable(false);
        battleLog.setFont(new Font("Monospaced", Font.PLAIN, 13));
        battleLog.setBackground(Color.BLACK);
        battleLog.setForeground(Color.GREEN);
        JScrollPane scrollPane = new JScrollPane(battleLog);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
    
        

        JPanel bottomPanel = new JPanel();
        rollButton = new JButton("⚔ Roll Dice!");
        rollButton.setFont(new Font("Arial", Font.BOLD, 16));
        rollButton.setPreferredSize(new Dimension(200, 50));
        rollButton.addActionListener(e -> takeTurn());
        bottomPanel.add(rollButton);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(statsPanel, BorderLayout.CENTER);
        frame.add(scrollPane, BorderLayout.SOUTH);
        frame.add(bottomPanel, BorderLayout.PAGE_END);
        frame.setVisible(true);

        log("Floor 1 — A " + currentEnemy.getName() + " appears!\n");
    }

    private void takeTurn() {
        if (!player.isAlive() || !currentEnemy.isAlive()) return;

        String result = engine.determineOutcome(player, currentEnemy);
        log(result);
        updateDisplay();

        if (!currentEnemy.isAlive()) {
            log("Enemy defeated!\n");
            rollButton.setEnabled(false);
            showRewardScreen();
            return;
        }

        if (!player.isAlive()) {
            log("You have been defeated...\n");
            rollButton.setEnabled(false);
            showGameOver();
        }
    }

    private void showRewardScreen() {
        String[] options = {"Heal 30 HP", "Increase Damage", "Proceed"};
        int choice = JOptionPane.showOptionDialog(frame,
            "Victory! Choose a reward:",
            "Floor Clear!",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null, options, options[0]);

        if (choice == 0) {
            player.applyReward(30);
            log("Healed 30 HP.\n");
        } else if (choice == 1) {
            log("Damage increased!\n");
        }

        engine.nextFloor();
        currentEnemy = spawnEnemy();
        rollButton.setEnabled(true);

        floorDisplay.setText("Floor " + engine.getFloorCount());
        enemyLabel.setText("Enemy: " + currentEnemy.getName());
        enemyHPBar.setMaximum(currentEnemy.getHp());
        enemyHPBar.setValue(currentEnemy.getHp());
        enemyHPBar.setString("Enemy HP: " + currentEnemy.getHp());

        log("\n--- Floor " + engine.getFloorCount() + " ---\n");
        log("A " + currentEnemy.getName() + " appears!\n");
        updateDisplay();
    }

    private void showGameOver() {
        int choice = JOptionPane.showConfirmDialog(frame,
            "You died on floor " + engine.getFloorCount() + ".\nPlay again?",
            "Game Over",
            JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            frame.dispose();
            new GameGUI(new GameEngine(), new Player());
        } else {
            System.exit(0);
        }
    }

    private void updateDisplay() {
        playerHPBar.setValue(Math.max(player.getHp(), 0));
        playerHPBar.setString("Player HP: " + Math.max(player.getHp(), 0));
        enemyHPBar.setValue(Math.max(currentEnemy.getHp(), 0));
        enemyHPBar.setString("Enemy HP: " + Math.max(currentEnemy.getHp(), 0));
    }

    private void log(String text) {
        battleLog.append(text);
        battleLog.setCaretPosition(battleLog.getDocument().getLength());
    }
}