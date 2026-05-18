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

//Many things still missin, needs better balancing (can only reach floor 8 if really lucky)
//Needs more buff options for the player
//Needs more enemy variety
//Needs more player variety
//Maybe change up the UI more
//Needs to make it easier for the player aswell

