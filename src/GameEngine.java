import java.util.Random;

public class GameEngine {
    private int floorCount = 1;
    private double enemyDmgMod = 1.0;
    private double enemyDodgeChance = 0.1;
    private Random rand = new Random();

    public int roll2d6() {
        return (rand.nextInt(6) + 1) + (rand.nextInt(6) + 1);
    }

    public String determineOutcome(Player player, Combatant enemy) {
        int roll = roll2d6();
        StringBuilder log = new StringBuilder();
        log.append("You rolled: ").append(roll).append("\n");

        if (roll >= 7) {
            boolean isCrit = Math.random() < player.getCritMultiplier();
            int dmg = isCrit
                ? (int)(player.getBaseDmg() * 1.5)
                : player.getBaseDmg();
            enemy.takeDamage(dmg);
            log.append(isCrit ? "CRITICAL HIT! " : "Hit! ");
            log.append("Dealt ").append(dmg).append(" damage.\n");
        } else {
            log.append("You missed!\n");
        }

        if (enemy.isAlive()) {
            boolean playerDodged = player.checkDodge();
            if (playerDodged) {
                log.append("Enemy attacked but you dodged!\n");
            } else {
                int enemyDmg = (int)(((Enemy) enemy).attack() * enemyDmgMod);
                player.takeDamage(enemyDmg);
                log.append("Enemy hit you for ").append(enemyDmg).append(" damage.\n");
            }
        }

        return log.toString();
    }

    public void nextFloor() {
        floorCount++;
        enemyDmgMod += 0.15;
    }

    public boolean isBossFloor() {
        return floorCount % 5 == 0;
    }

    public int getFloorCount() { return floorCount; }
    public double getEnemyDmgMod() { return enemyDmgMod; }
}