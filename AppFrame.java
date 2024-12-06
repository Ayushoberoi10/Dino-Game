import javax.swing.*;

public class AppFrame extends JFrame {
    public AppFrame() {
        this.setTitle("Chrome Dino Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 300);
        this.setResizable(false);

        AppPanel gamePanel = new AppPanel();
        this.add(gamePanel);

        this.setVisible(true);
    }
}
