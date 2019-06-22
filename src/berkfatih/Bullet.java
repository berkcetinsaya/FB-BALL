package berkfatih;

import java.awt.*;

public class Bullet {

	// FIELDS
	private int x;
	private int y;
	private int r;

	private double dy;
	private double rad;
	private double speed;

	private Color color1;

	// CONSTRUCTOR
	public Bullet(double angle, int x, int y) {
		this.x = x;
		this.y = y;

		r = 2;

		rad = Math.toRadians(angle);
		speed = 10;

		dy = Math.sin(rad) * speed;

		color1 = Color.WHITE;

	}

	// FUNCTIONS
	public int getx() {
		return x;
	}

	public int gety() {
		return y;
	}

	public int getr() {
		return r;
	}

	public boolean update() {
		y += dy;

		if (y < -r || y > GamePanel.HEIGHT + r) {
			return true;
		}
		return false;
	}

	public void draw(Graphics2D g) {
		g.setColor(color1);
		g.fillOval(x, y, 2 * r, 2 * r);
	}
}
