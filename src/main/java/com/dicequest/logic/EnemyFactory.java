package com.dicequest.logic;

import com.dicequest.entities.*;

public class EnemyFactory {
    public static final int BOSS_FLOOR_INTERVAL = 5;
    public static final int ENEMY_HP_INCREMENT = 5;
    public static final int ENEMY_DAMAGE_INCREMENT = 2;

    private EnemyFactory() {}

    public static Enemy createForFloor(int floor) {
        if (floor % BOSS_FLOOR_INTERVAL == 0) {
            return new Boss();
        }
        String name = getEnemyName(floor);
        int hp = 60 + (floor * ENEMY_HP_INCREMENT);
        int damage = 8 + (floor * ENEMY_DAMAGE_INCREMENT);
        return new Enemy(name, hp, damage);
    }

    private static String getEnemyName(int floor) {
        if (floor <= 3) return "Goblin";
        if (floor <= 6) return "Orc";
        if (floor <= 9) return "Troll";
        return "Demon";
    }
}