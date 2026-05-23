package com.dicequest.logic;

import com.dicequest.entities.*;

public class EnemyFactory {

    private EnemyFactory() {}

    public static Enemy createForFloor(int floor) {
        if (floor % 5 == 0) {
            return new Boss();
        }
        String name = getEnemyName(floor);
        int hp = 60 + (floor * 5);
        int damage = 8 + (floor * 2);
        return new Enemy(name, hp, damage);
    }

    private static String getEnemyName(int floor) {
        if (floor <= 3) return "Goblin";
        if (floor <= 6) return "Orc";
        if (floor <= 9) return "Troll";
        return "Demon";
    }
}