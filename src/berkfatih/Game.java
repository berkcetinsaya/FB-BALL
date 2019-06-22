package berkfatih;

import java.awt.*;
import java.awt.image.*;

import javax.swing.*;

public class Game {
	public static Entrance e = new Entrance();
	public static JFrame game = new JFrame("FB-BALL");
	public static void main(String[] args) {
		e.setMaximumSize(new Dimension(GamePanel.WIDTH / 2, GamePanel.HEIGHT - 200));
		e.setMinimumSize(new Dimension(GamePanel.WIDTH / 2, GamePanel.HEIGHT - 200));
		e.setSize(GamePanel.WIDTH / 2, GamePanel.HEIGHT - 200);
		e.setResizable(false);
		e.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		e.setLocationRelativeTo(null);
		e.setVisible(true);

		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.setMaximumSize(new Dimension(GamePanel.WIDTH, GamePanel.HEIGHT));
		game.setMinimumSize(new Dimension(GamePanel.WIDTH, GamePanel.HEIGHT));
		game.setContentPane(new GamePanel());
		game.setResizable(false);
		game.pack();
		game.setLocationRelativeTo(null);
		game.setCursor(game.getToolkit().createCustomCursor(new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "null"));
	}
}
