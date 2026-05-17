package com.dicequest;

import com.dicequest.logic.GameController;
import com.dicequest.gui.GameGUI;
import com.dicequest.gui.GameDisplay;

public class Main {
    public static void main(String[] args) {
        GameController controller = new GameController();
        GameGUI gui = new GameGUI();
        GameDisplay display = new GameDisplay(gui, controller);
        controller.setView(display);
        display.updateDisplay(controller.getState());
        display.updateBattleLog("Floor 1 — A " +
            controller.getState().getCurrentEnemy().getName() + " appears!\n");
    }
}