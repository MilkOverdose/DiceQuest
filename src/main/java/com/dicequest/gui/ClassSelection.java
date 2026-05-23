package com.dicequest.gui;
import com.dicequest.logic.GameLogic;

import javax.swing.*;
import java.awt.*;

public class ClassSelection extends JFrame {

    public ClassSelection() {
        super("Dice Quest — Choose Your Class");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 820);
        setResizable(false);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Theme.BG_DARK);
        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 10));
        mainPanel.setBackground(Theme.BG_DARK);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Theme.BG_DARK);

        JLabel title = new JLabel("CHOOSE YOUR CLASS");
        title.setFont(new Font("Georgia", Font.BOLD, 22));
        title.setForeground(Theme.GOLD);
        title.setAlignmentX(CENTER_ALIGNMENT);

        headerPanel.add(title);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(Theme.buildDivider());

        String knightSkillsDescription = """
            📊  RESOLVE STACKS
            ==============================================
            • STACKS:  Max capacity of 5 stacks.
            • PASSIVE: Grants +5% Damage Reduction per stack.
            • SAFETY:  At 5 stacks, lethal damage is blocked,
                       resetting stacks to 0 and setting HP to 10.

            ⚔️  BASIC ATTACK
            ==============================================
            • EFFECT:  Hits automatically for 100% Base Dmg.

            🛡️  PARRY & RIPOSTE
            ==============================================
            • STANCE:  Deflects enemy damage.
            • BONUS:   Triggers a powerful counter attack
                       and generates +1 Resolve Stack.

            🔨  POMMEL STRIKE
            ==============================================
            • COST:    Consumes 2 Resolve Stacks.
            • EFFECT:  Hits and STUNS your target.
            • PASSIVE: Gives 5% permanent damage reduction
                       for the battle up to 25%.

            🪓  HEAD SPLITTER
            ==============================================
            • ACC:     Success scales up with Resolve Stacks.
            • EFFECT:  Massive high-roll critical multiplier.
            • RISK:    Missing triggers stasis penalties.
            """;

        JPanel knightCard = buildClassCard(
            "KNIGHT",
            "HP: 150  |  BASE DMG: 30  |  AGILITY: 30",
            knightSkillsDescription,
            () -> launchGame()
        );

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(knightCard, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel buildClassCard(String className, String stats, String skills, Runnable onSelect) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Theme.BG_PANEL);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.GOLD_DIM, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel nameLabel = new JLabel(className);
        nameLabel.setFont(new Font("Georgia", Font.BOLD, 18));
        nameLabel.setForeground(Theme.GOLD);
        nameLabel.setAlignmentX(LEFT_ALIGNMENT);

        JLabel statsLabel = new JLabel(stats);
        statsLabel.setFont(Theme.LABEL_FONT);
        statsLabel.setForeground(Color.LIGHT_GRAY);
        statsLabel.setAlignmentX(LEFT_ALIGNMENT);

        JTextArea skillsArea = new JTextArea(skills);
        skillsArea.setEditable(false);
        skillsArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        skillsArea.setBackground(Theme.BG_PANEL);
        skillsArea.setForeground(new Color(200, 200, 200));
        skillsArea.setAlignmentX(LEFT_ALIGNMENT);
        skillsArea.setLineWrap(false);
        skillsArea.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        MenuButton selectButton = new MenuButton("SELECT");
        selectButton.setAlignmentX(LEFT_ALIGNMENT);
        selectButton.addActionListener(e -> {
            dispose();
            onSelect.run();
        });

        card.add(nameLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(statsLabel);
        card.add(Box.createVerticalStrut(12));
        card.add(skillsArea);
        card.add(Box.createVerticalStrut(15));
        card.add(selectButton);

        return card;
    }

    private void launchGame() {
        GameLogic gameLogic = new GameLogic();
        GameWindow window = new GameWindow();
        UIUpdater updater = new UIUpdater(window, gameLogic);
        gameLogic.setScreen(updater);
        updater.updateDisplay(gameLogic.getState());
        updater.updateBattleLog("Floor 1 — A " +
            gameLogic.getState().getCurrentEnemy().getName() + " appears!\n");
    }
}