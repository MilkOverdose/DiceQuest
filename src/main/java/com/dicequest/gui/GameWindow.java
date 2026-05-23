package com.dicequest.gui;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {
    private JProgressBar playerHPBar;
    private JProgressBar enemyHPBar;
    private JLabel floorDisplay;
    private JLabel enemyLabel;
    private JTextArea battleLog;
    private JLabel playerLabel;

    private JTextArea skillDescriptionBox;

    private JLabel lblResolveStacks;

    // Skill Selection Buttons
    private JButton btnBasicAttack;
    private JButton btnParryRiposte;
    private JButton btnPommelStrike;
    private JButton btnHeadSplitter;
    private String selectedActionTag = "BASIC";

    private RollButton rollButton;

    public GameWindow() {
        super("Dice Quest");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 840);
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
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        mainPanel.add(buildFloorDisplay());
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(buildDivider());
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(buildEnemyPanel());
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(buildPlayerPanel());
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(buildDivider());
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(buildBattleLog());
        mainPanel.add(Box.createVerticalStrut(12));

        mainPanel.add(buildStatusDisplayRow());
        mainPanel.add(Box.createVerticalStrut(10));

        mainPanel.add(buildSkillDescriptionPanel());
        mainPanel.add(Box.createVerticalStrut(12));

        mainPanel.add(buildSkillsSelectionGrid());
        mainPanel.add(Box.createVerticalStrut(15));

        rollButton = new RollButton();
        rollButton.setAlignmentX(CENTER_ALIGNMENT);
        mainPanel.add(rollButton);

        add(mainPanel);
    }

    private JLabel buildFloorDisplay() {
        floorDisplay = new JLabel("");
        floorDisplay.setFont(Theme.TITLE_FONT);
        floorDisplay.setForeground(Theme.GOLD);
        floorDisplay.setAlignmentX(CENTER_ALIGNMENT);
        return floorDisplay;
    }

    private JPanel buildEnemyPanel() {
        JPanel panel = createCombatantPanel();
        enemyLabel = new JLabel("");
        enemyLabel.setFont(Theme.LABEL_FONT);
        enemyLabel.setForeground(Theme.TEXT_ENEMY);
        enemyHPBar = createHPBar(1, Theme.HP_RED, Theme.HP_RED_BG);

        panel.add(enemyLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(enemyHPBar);
        return panel;
    }

    private JPanel buildPlayerPanel() {
        JPanel panel = createCombatantPanel();
        playerLabel = new JLabel("");
        playerLabel.setFont(Theme.LABEL_FONT);
        playerLabel.setForeground(Theme.TEXT_PLAYER);
        playerHPBar = createHPBar(1, Theme.HP_GREEN, Theme.HP_GREEN_BG);

        panel.add(playerLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(playerHPBar);
        return panel;
    }

    private JPanel createCombatantPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Theme.BG_PANEL);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.GOLD_DIM, 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        return panel;
    }

    private JProgressBar createHPBar(int maxHp, Color fg, Color bg) {
        JProgressBar bar = new JProgressBar(0, maxHp);
        bar.setValue(maxHp);
        bar.setStringPainted(true);
        bar.setForeground(fg);
        bar.setBackground(bg);
        bar.setFont(Theme.LABEL_FONT);
        bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        return bar;
    }

    private JScrollPane buildBattleLog() {
        battleLog = new JTextArea(5, 30);
        battleLog.setEditable(false);
        battleLog.setFont(Theme.LOG_FONT);
        battleLog.setBackground(Theme.LOG_BG);
        battleLog.setForeground(Theme.LOG_TEXT);
        battleLog.setLineWrap(true);
        battleLog.setWrapStyleWord(true);
        battleLog.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JScrollPane scrollPane = new JScrollPane(battleLog);
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        scrollPane.setBorder(BorderFactory.createLineBorder(Theme.GOLD_DIM, 1));
        return scrollPane;
    }

    private JPanel buildStatusDisplayRow() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.BG_DARK);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));

        lblResolveStacks = new JLabel("");
        lblResolveStacks.setFont(new Font("Monospaced", Font.BOLD, 12));
        lblResolveStacks.setForeground(Theme.GOLD);

        panel.add(lblResolveStacks, BorderLayout.WEST);
        return panel;
    }

    // New Panel Factory Method
    private JPanel buildSkillDescriptionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.BG_PANEL);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.GOLD_DIM, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        skillDescriptionBox = new JTextArea();
        skillDescriptionBox.setEditable(false);
        skillDescriptionBox.setLineWrap(true);
        skillDescriptionBox.setWrapStyleWord(true);
        skillDescriptionBox.setBackground(Theme.BG_PANEL);
        skillDescriptionBox.setForeground(Color.LIGHT_GRAY);
        skillDescriptionBox.setFont(new Font("SansSerif", Font.ITALIC, 12));

        panel.add(skillDescriptionBox, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildSkillsSelectionGrid() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBackground(Theme.BG_DARK);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 95));

        btnBasicAttack = createSkillButton("⚔ Basic Attack", "BASIC");
        btnParryRiposte = createSkillButton("🛡 Parry & Riposte", "PARRY");
        btnPommelStrike = createSkillButton("🔨 Pommel Strike", "POMMEL");
        btnHeadSplitter = createSkillButton("🪓 Head Splitter", "SPLITTER");

        panel.add(btnBasicAttack);
        panel.add(btnParryRiposte);
        panel.add(btnPommelStrike);
        panel.add(btnHeadSplitter);

        refreshButtonVisuals();
        return panel;
    }

    private JButton createSkillButton(String text, String actionTag) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Georgia", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            this.selectedActionTag = actionTag;
            refreshButtonVisuals();
        });
        return btn;
    }

    private void refreshButtonVisuals() {
        updateSingleButtonColor(btnBasicAttack, "BASIC");
        updateSingleButtonColor(btnParryRiposte, "PARRY");
        updateSingleButtonColor(btnPommelStrike, "POMMEL");
        updateSingleButtonColor(btnHeadSplitter, "SPLITTER");
        updateDescriptionText(); // Updates text content area in sync with selection clicks
    }

    private void updateSingleButtonColor(JButton btn, String targetTag) {
        if (selectedActionTag.equals(targetTag)) {
            btn.setBackground(Theme.GOLD);
            btn.setForeground(Theme.BG_DARK);
            btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        } else {
            btn.setBackground(Theme.BG_PANEL);
            btn.setForeground(Color.LIGHT_GRAY);
            btn.setBorder(BorderFactory.createLineBorder(Theme.GOLD_DIM, 1));
        }
    }

    // New Internal Text Assigner Matrix
    private void updateDescriptionText() {
        String description = switch (selectedActionTag) {
            case "BASIC" -> """
                    ⚔️  BASIC ATTACK
                    ================================================================
                    • EFFECT:  Hits automatically for 100% of your Base Damage.
                    • BONUS:   Generates +1 Resolve Stack upon successful execution.
                    """;
            case "PARRY" -> """
                    🛡️  PARRY & RIPOSTE
                    ================================================================
                    • MECHANIC: Enters a temporary high-defense parry stance.
                    • EFFECT:   Deflects the enemy strike completely on a successful d6 roll.
                    • BONUS:    Triggers a powerful riposte counter-strike payload.
                    """;
            case "POMMEL" -> """
                    🔨  POMMEL STRIKE
                    ================================================================
                    • COST:     Consumes 2 Resolve Stacks to activate.
                    • EFFECT:   Hits automatically and completely STUNS your target.
                    • BONUS:    Forces the enemy to completely skip their next counter-turn.
                    """;
            case "SPLITTER" -> """
                    🪓  HEAD SPLITTER
                    ================================================================
                    • ACCURACY: Hit threshold scales downward based on current Resolve Stacks.
                    • EFFECT:   Deals massive critical high-roll multiplier damage on hit.
                    • PENALTY:  Missing leaves you off-balance, inducing a stasis turn penalty.
                    """;
            default -> "";
        };
        skillDescriptionBox.setText(description);
    }

    private JSeparator buildDivider() {
        JSeparator divider = new JSeparator();
        divider.setForeground(Theme.GOLD_DIM);
        divider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        return divider;
    }

    public void setControlsEnabled(boolean enabled) {
        rollButton.setEnabled(enabled);
        btnBasicAttack.setEnabled(enabled);
        btnParryRiposte.setEnabled(enabled);
        btnPommelStrike.setEnabled(enabled);
        btnHeadSplitter.setEnabled(enabled);
    }

    public String getSelectedActionTag() {
        return this.selectedActionTag;
    }

    // Getters
    public RollButton getRollButton() {
        return rollButton;
    }

    public JLabel getLblResolveStacks() {
        return lblResolveStacks;
    }

    public JProgressBar getPlayerHPBar() {
        return playerHPBar;
    }

    public JProgressBar getEnemyHPBar() {
        return enemyHPBar;
    }

    public JLabel getFloorDisplay() {
        return floorDisplay;
    }

    public JLabel getEnemyLabel() {
        return enemyLabel;
    }

    public JTextArea getBattleLog() {
        return battleLog;
    }

    public JLabel getPlayerLabel() {
        return playerLabel;
    }
}