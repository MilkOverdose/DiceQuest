package com.dicequest.gui;

import javax.swing.*;
import java.awt.*;

public class GameGUI {
    private JFrame frame;
    private JButton rollButton;
    private JProgressBar playerHPBar;
    private JProgressBar enemyHPBar;
    private JLabel floorDisplay;
    private JLabel enemyLabel;
    private JTextArea battleLog;

    public GameGUI() {
        buildUI();
    }

    private void buildUI() {
        frame = new JFrame("Dice Quest");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 700);
        frame.setResizable(false);
        frame.getContentPane().setBackground(GameDisplay.BG_DARK);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(GameDisplay.BG_DARK);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // Floor title
        floorDisplay = new JLabel("⚔ Floor 1 ⚔");
        floorDisplay.setFont(GameDisplay.TITLE_FONT);
        floorDisplay.setForeground(GameDisplay.GOLD);
        floorDisplay.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Dividers
        JSeparator divider1 = buildDivider();
        JSeparator divider2 = buildDivider();

        // Enemy panel
        JPanel enemyPanel = new JPanel();
        enemyPanel.setLayout(new BoxLayout(enemyPanel, BoxLayout.Y_AXIS));
        enemyPanel.setBackground(GameDisplay.BG_PANEL);
        enemyPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GameDisplay.GOLD_DIM, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        enemyPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        enemyLabel = new JLabel("Goblin");
        enemyLabel.setFont(GameDisplay.LABEL_FONT);
        enemyLabel.setForeground(new Color(220, 100, 100));

        enemyHPBar = new JProgressBar(0, 65);
        enemyHPBar.setValue(65);
        enemyHPBar.setStringPainted(true);
        enemyHPBar.setForeground(GameDisplay.HP_RED);
        enemyHPBar.setBackground(new Color(40, 20, 20));
        enemyHPBar.setFont(GameDisplay.LABEL_FONT);
        enemyHPBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

        enemyPanel.add(enemyLabel);
        enemyPanel.add(Box.createVerticalStrut(5));
        enemyPanel.add(enemyHPBar);

        // Player panel
        JPanel playerPanel = new JPanel();
        playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));
        playerPanel.setBackground(GameDisplay.BG_PANEL);
        playerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GameDisplay.GOLD_DIM, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        playerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel playerLabel = new JLabel("Player");
        playerLabel.setFont(GameDisplay.LABEL_FONT);
        playerLabel.setForeground(new Color(100, 200, 120));

        playerHPBar = new JProgressBar(0, 150);
        playerHPBar.setValue(150);
        playerHPBar.setStringPainted(true);
        playerHPBar.setForeground(GameDisplay.HP_GREEN);
        playerHPBar.setBackground(new Color(20, 40, 20));
        playerHPBar.setFont(GameDisplay.LABEL_FONT);
        playerHPBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

        playerPanel.add(playerLabel);
        playerPanel.add(Box.createVerticalStrut(5));
        playerPanel.add(playerHPBar);

        // Battle log
        battleLog = new JTextArea(8, 30);
        battleLog.setEditable(false);
        battleLog.setFont(GameDisplay.LOG_FONT);
        battleLog.setBackground(new Color(12, 12, 18));
        battleLog.setForeground(new Color(180, 220, 180));
        battleLog.setLineWrap(true);
        battleLog.setWrapStyleWord(true);
        battleLog.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JScrollPane scrollPane = new JScrollPane(battleLog);
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        scrollPane.setBorder(BorderFactory.createLineBorder(GameDisplay.GOLD_DIM, 1));

        // Roll button — no action listener here, set from outside
        rollButton = buildRollButton();

        // Assemble
        mainPanel.add(floorDisplay);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(divider1);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(enemyPanel);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(playerPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(divider2);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(scrollPane);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(rollButton);

        frame.add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JSeparator buildDivider() {
        JSeparator divider = new JSeparator();
        divider.setForeground(GameDisplay.GOLD_DIM);
        divider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        return divider;
    }

    private JButton buildRollButton() {
        JButton button = new JButton("⚔  ROLL DICE") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(GameDisplay.GOLD_DIM);
                } else if (getModel().isRollover()) {
                    g2.setColor(GameDisplay.GOLD.brighter());
                } else {
                    g2.setColor(GameDisplay.GOLD);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(GameDisplay.BG_DARK);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };

        button.setFont(new Font("Georgia", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(200, 48));
        button.setMaximumSize(new Dimension(200, 48));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    // Getters for GameDisplay to hook into
    public JFrame getFrame() { return frame; }
    public JButton getRollButton() { return rollButton; }
    public JProgressBar getPlayerHPBar() { return playerHPBar; }
    public JProgressBar getEnemyHPBar() { return enemyHPBar; }
    public JLabel getFloorDisplay() { return floorDisplay; }
    public JLabel getEnemyLabel() { return enemyLabel; }
    public JTextArea getBattleLog() { return battleLog; }
}