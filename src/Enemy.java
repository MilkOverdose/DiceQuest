public class Enemy implements Combatant {
    private int hp = 60;
    private int baseDmg;

    public Enemy(int baseDmg) {
        this.baseDmg = baseDmg;
    }

    @Override
    public void takeDamage(int amount) {
        hp -= amount;
    }

    @Override
    public boolean isAlive() {
        return hp > 0;
    }

    public int attack() {
        return baseDmg;
    }

    public int getHp() { return hp; }
}