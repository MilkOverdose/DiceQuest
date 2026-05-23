package com.dicequest.gui;

import java.awt.*;

public class RollButton extends MenuButton {

    private static final int BUTTON_WIDTH  = 200;
    private static final int BUTTON_HEIGHT = 48;

    public RollButton() {
        super("⚔  ROLL DICE");
        setFont(Theme.BUTTON_FONT);
        setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
    }
}