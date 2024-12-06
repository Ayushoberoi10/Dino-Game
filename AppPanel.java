import java.awt.*;
import java.awt.event.*;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import javax.swing.*;

public class AppPanel extends JPanel implements KeyListener, ActionListener {
    private Timer timer;
    private int playerY = 200; // Player's Y position
    private int velocityY = 0; // Player's vertical velocity
    private boolean isJumping = false;

    private ArrayList<Rectangle> obstacles;
    private int obstacleSpeed = 5;

    private Image playerImage;
    private Image obstacleImage;

    public AppPanel() {
        this.setFocusable(true);
        this.addKeyListener(this);

        // Load images
        playerImage = new ImageIcon("dino.png").getImage();
        obstacleImage = new ImageIcon("cactus.png").getImage();

        // Initialize game variables
        resetGame();
    }

    private void resetGame() {
        playerY = 200;
        velocityY = 0;
        isJumping = false;
        obstacles = new ArrayList<>();
        if (timer != null) {
            timer.stop();
        }
        timer = new Timer(20, this);
        timer.start();
        spawnObstacle();
        this.requestFocusInWindow(); // Ensure the panel has focus
        repaint(); // Force the panel to repaint
    }

    private void spawnObstacle() {
        obstacles.add(new Rectangle(800, 200, 50, 50));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Game loop logic
        playerY += velocityY;
        if (isJumping) {
            velocityY += 1; // Gravity effect
        }
        if (playerY >= 200) {
            playerY = 200; // Ground level
            isJumping = false;
            velocityY = 0;
        }

        // Move obstacles
        for (int i = 0; i < obstacles.size(); i++) {
            Rectangle obstacle = obstacles.get(i);
            obstacle.x -= obstacleSpeed;
            if (obstacle.x + obstacle.width < 0) {
                obstacles.remove(i);
                spawnObstacle();
            }
        }

        // Check for collisions
        for (Rectangle obstacle : obstacles) {
            if (new Rectangle(50, playerY, 50, 50).intersects(obstacle)) {
                timer.stop();
                retryGame();
                return;
            }
        }

        repaint();
    }

    private void retryGame() {
        SwingUtilities.invokeLater(() -> {
            int retry = JOptionPane.showConfirmDialog(
                    this,
                    "Game Over! Retry?",
                    "Game Over",
                    JOptionPane.YES_NO_OPTION
            );
            if (retry == JOptionPane.YES_OPTION) {
                resetGame();
            } else {
                System.exit(0); // Exit the application
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw player
        g2d.drawImage(playerImage, 50, playerY, 50, 50, (ImageObserver) this);

        // Draw obstacles
        for (Rectangle obstacle : obstacles) {
            g2d.drawImage(obstacleImage, obstacle.x, obstacle.y, obstacle.width, obstacle.height, (ImageObserver) this);
        }

        // Draw ground
        g2d.setColor(Color.BLACK);
        g2d.drawLine(0, 250, 800, 250);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE && !isJumping) {
            velocityY = -15; // Jump impulse
            isJumping = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
