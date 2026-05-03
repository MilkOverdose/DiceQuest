import java.util.Random;

public class Boss extends Enemy {
    private Random rand = new Random();

    public Boss() {
        super(0);  
    }

    @Override
    public int attack() {
        return rollBossDamage();
    }

    public int rollBossDamage() {
        return (rand.nextInt(6) + 1) + (rand.nextInt(6) + 1);
    }

    public void applyBossDebuff(Player player) {
        player.takeDamage(5);  
    }
}