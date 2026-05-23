package com.dicequest.gui;

import javax.swing.*;
import java.awt.*;

public final class Theme {
    public static final Color BG_DARK    = new Color(18, 18, 24);
    public static final Color BG_PANEL   = new Color(28, 28, 38);
    public static final Color GOLD       = new Color(212, 175, 55);
    public static final Color GOLD_DIM   = new Color(140, 110, 30);
    public static final Color HP_RED     = new Color(180, 40, 40);
    public static final Color HP_GREEN   = new Color(40, 180, 80);
    public static final Color TEXT_ENEMY  = new Color(220, 100, 100);
    public static final Color TEXT_PLAYER = new Color(100, 200, 120);
    public static final Color LOG_BG     = new Color(12, 12, 18);
    public static final Color LOG_TEXT   = new Color(180, 220, 180);
    public static final Color HP_RED_BG   = new Color(40, 20, 20);
    public static final Color HP_GREEN_BG = new Color(20, 40, 20);

    public static final Font TITLE_FONT  = new Font("Georgia", Font.BOLD, 20);
    public static final Font LABEL_FONT  = new Font("Georgia", Font.BOLD, 13);
    public static final Font LOG_FONT    = new Font("Monospaced", Font.PLAIN, 12);
    public static final Font BUTTON_FONT = new Font("Georgia", Font.BOLD, 16);

    public static JSeparator buildDivider() {
        JSeparator divider = new JSeparator();
        divider.setForeground(GOLD_DIM);
        divider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        return divider;
    }

    private Theme() {}
}