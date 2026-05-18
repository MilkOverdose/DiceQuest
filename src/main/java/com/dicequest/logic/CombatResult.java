package com.dicequest.logic;

public class CombatResult { // holds result of every turn
    private int roll;
    private boolean playerHit;
    private boolean isCrit;
    private int playerDamage;
    private boolean playerDodged;
    private int enemyDamage;
    private int debuffDamage;

    public String getSummary(String enemyName) { //builds readable string of of combat data probably should be in GUI
        StringBuilder sb = new StringBuilder();
        sb.append("You rolled: ").append(roll).append("\n");

        if (playerHit) {
            sb.append(isCrit ? "CRITICAL HIT! " : "Hit! ");
            sb.append("Dealt ").append(playerDamage).append(" damage.\n");
        } else {
            sb.append("You missed!\n");
        }

        if (playerDodged) {
            sb.append(enemyName).append(" attacked but you dodged!\n");
        } else if (enemyDamage > 0) {
            sb.append(enemyName).append(" hit you for ")
              .append(enemyDamage).append(" damage.\n");
        }

        if (debuffDamage > 0) {
            sb.append(enemyName).append(" applied a debuff! ")
              .append("You take ").append(debuffDamage).append(" extra damage.\n");
        }

        return sb.toString();
    }

    public void setRoll(int roll) { this.roll = roll; }
    public void setPlayerHit(boolean hit) { this.playerHit = hit; }
    public void setCrit(boolean crit) { this.isCrit = crit; }
    public void setPlayerDamage(int dmg) { this.playerDamage = dmg; }
    public void setPlayerDodged(boolean dodged) { this.playerDodged = dodged; }
    public void setEnemyDamage(int dmg) { this.enemyDamage = dmg; }
    public void setDebuffDamage(int dmg) { this.debuffDamage = dmg; }
    public int getRoll() { return roll; }
    public boolean isPlayerHit() { return playerHit; }
    public boolean isCrit() { return isCrit; }
    public int getPlayerDamage() { return playerDamage; }
    public boolean isPlayerDodged() { return playerDodged; }
    public int getEnemyDamage() { return enemyDamage; }
    public int getDebuffDamage() { return debuffDamage; }
}