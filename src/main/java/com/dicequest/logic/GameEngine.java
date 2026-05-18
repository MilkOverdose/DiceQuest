package com.dicequest.logic;

import com.dicequest.entities.*;


public class GameEngine { //combat calculator basically
    private static final int HIT_THRESHOLD = 7;
    private double enemyDmgMod = 1.0;

    public CombatResult processTurn(Player player, Enemy enemy) { //rolls die checks if its crit makes enemy attack back checks dodge  
        CombatResult result = new CombatResult();                 //boss debuff if applicable and returns combat results
        int roll = Dice.roll2d6();
        result.setRoll(roll);

        // Player attacks
        if (roll >= HIT_THRESHOLD) { 
            boolean isCrit = Math.random() < player.getCritMultiplier();
            int dmg = isCrit
                ? (int)(player.getBaseDmg() * 1.5)
                : player.getBaseDmg();
            enemy.takeDamage(dmg);
            result.setPlayerDamage(dmg);
            result.setCrit(isCrit);
            result.setPlayerHit(true);
        } else {
            result.setPlayerHit(false);
        }

        // Enemy attacks back if alive
        if (enemy.isAlive()) {
            boolean dodged = player.checkDodge();
            result.setPlayerDodged(dodged);

            if (!dodged) {
                int enemyDmg = (int)(enemy.attack() * enemyDmgMod);
                player.takeDamage(enemyDmg);
                result.setEnemyDamage(enemyDmg);

                // Boss debuff only triggers every other turn
                if (enemy instanceof Boss boss && boss.shouldApplyDebuff()) {
                    int debuffDmg = boss.getDebuffDamage();
                    player.takeDamage(debuffDmg);
                    result.setDebuffDamage(debuffDmg);
                }
            }
        }

        return result;
    }

    public void scaleDifficulty() {
        enemyDmgMod += 0.15;
    }

    public double getEnemyDmgMod() { return enemyDmgMod; }
}