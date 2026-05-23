package com.dicequest.gui;

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
        mainPanel.add(buildDivider());
        mainPanel.add(Box.createVerticalStrut(40));
        mainPanel.add(buildStartButton());
        mainPanel.add(Box.createVerticalStrut(15));
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

    private JSeparator buildDivider() {
        JSeparator divider = new JSeparator();
        divider.setForeground(Theme.GOLD_DIM);
        divider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        return divider;
    }

    private MenuButton buildStartButton() {
        MenuButton startButton = new MenuButton("START GAME");
        startButton.addActionListener(e -> {
            dispose();
            new ClassSelection();
        });
        return startButton;
    }

    private MenuButton buildQuitButton() {
        MenuButton quitButton = new MenuButton("QUIT");
        quitButton.addActionListener(e -> System.exit(0));
        return quitButton;
    }
}