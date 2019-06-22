package berkfatih;

import java.awt.*;

public class Ball {

	public int x = GamePanel.WIDTH / 2;
	public int y = GamePanel.HEIGHT - 16;
	private int r;

	public double dx = 4;
	public double dy = 7;

	private boolean slow;
	private boolean fast = false;

	private Color color1;

	public Ball() {
		r = 3;
		color1 = Color.WHITE;
	}

	public int getx() {
		return x;
	}

	public int gety() {
		return y;
	}

	public int getr() {
		return r;
	}

	public void setSlow(boolean b) {
		slow = b;
	}

	public void setFast(boolean b) {
		fast = b;
	}

	public void update() {
		if (slow) {
			x += dx * 0.7;
			y -= dy * 0.7;
		}
		else if (fast) {
			x += dx * 1.3;
			y -= dy * 1.3;
		}
		else {
			x += dx;
			y -= dy;
		}
		if (x < 0 || x > GamePanel.WIDTH - 2 * r) {
			dx = -dx;
			GamePanel.s7.start();
		}
		if (y <= r) {
			dy = -dy;
			GamePanel.s7.start();
		}
	}

	public void draw(Graphics2D g) {
		if (fast) {
			g.setColor(Color.RED);
			g.fillOval(x, y, 2 * r, 2 * r);
		}
		else {
			g.setColor(color1);
			g.fillOval(x, y, 2 * r, 2 * r);
		}
	}
}