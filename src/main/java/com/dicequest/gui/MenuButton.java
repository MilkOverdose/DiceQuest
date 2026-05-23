package com.dicequest.gui;

import javax.swing.*;
import java.awt.*;

public class MenuButton extends JButton {

    public MenuButton(String text) {
        super(text);
        setFont(new Font("Georgia", Font.BOLD, 16));
        setPreferredSize(new Dimension(250, 52));
        setMaximumSize(new Dimension(250, 52));
        setAlignmentX(CENTER_ALIGNMENT);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        if (getModel().isPressed()) {
            g2.setColor(Theme.GOLD_DIM);
        } else if (getModel().isRollover()) {
            g2.setColor(Theme.GOLD.brighter());
        } else {
            g2.setColor(Theme.GOLD);
        }

        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
        g2.setColor(Theme.BG_DARK);
        g2.setFont(getFont());

        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(getText())) / 2;
        int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
        g2.drawString(getText(), x, y);
        g2.dispose();
    }
}