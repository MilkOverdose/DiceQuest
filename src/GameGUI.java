import javax.swing.*;

public class GameGUI {
    private JButton rollButton;
    private JProgressBar playerHPBar;
    private JLabel floorDisplay;
    private GameEngine engine;
    private Player player;
    private Enemy enemy;

    public GameGUI(GameEngine engine, Player player, Enemy enemy) {
        this.engine = engine;
        this.player = player;
        this.enemy = enemy;
        buildUI();
    }

    private void buildUI() {
        JFrame frame = new JFrame("Dice Quest");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        playerHPBar = new JProgressBar(0, 100);
        playerHPBar.setValue(player.getHp());
        playerHPBar.setStringPainted(true);

        floorDisplay = new JLabel("Floor: 1");

        rollButton = new JButton("Roll Dice!");
        rollButton.addActionListener(e -> {
            int roll = engine.roll2d6();
            engine.determineOutcome(roll, player, enemy);
            updateDisplay();
        });

        frame.add(floorDisplay);
        frame.add(playerHPBar);
        frame.add(rollButton);
        frame.setVisible(true);
    }

    public void updateDisplay() {
        playerHPBar.setValue(player.getHp());
        floorDisplay.setText("Floor: " + engine.getFloorCount());
    }

    public void showRewardScreen() {
        JOptionPane.showMessageDialog(null, "Enemy defeated! You gain 20 HP.");
        player.applyReward(20);
        engine.nextFloor();
        updateDisplay();
    }
}