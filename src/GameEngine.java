import java.util.Random;

public class GameEngine {
    private int floorCount = 1;
    private double enemyDmgMod = 1.0;
    private double enemyDodgeChance = 0.1;
    private Random rand = new Random();

    public int roll2d6() {
        return (rand.nextInt(6) + 1) + (rand.nextInt(6) + 1);
    }

    public void determineOutcome(int roll, Player player, Combatant enemy) {
        if (roll >= 7) {
            int dmg = player.getBaseDmg();
            enemy.takeDamage(dmg);
            System.out.println("Hit! Dealt " + dmg + " damage.");
        } else {
            System.out.println("Miss!");
        }
    }

    public void nextFloor() {
        floorCount++;
        enemyDmgMod += 0.1;
        System.out.println("Floor " + floorCount);
    }

    public int getFloorCount() { return floorCount; }
}