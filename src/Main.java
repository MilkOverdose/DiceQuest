public class Main {
    public static void main(String[] args) {
        GameEngine engine = new GameEngine();
        Player player = new Player();
        Enemy enemy = new Enemy(10);
        new GameGUI(engine, player, enemy);
    }
}