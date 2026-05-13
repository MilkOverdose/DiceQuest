public class Main {
    public static void main(String[] args) {
        GameEngine engine = new GameEngine();
        Player player = new Player();
        new GameGUI(engine, player);
    }
}