package berkfatih;

import java.awt.*;

public class Enemy {

	// FIELDS
	public int x;
	public int y;

	public int w;
	private int h;

	public int r;

	private int health;
	private int type;
	private int rank;

	private Color color1;

	private boolean dead;

	private boolean hit;
	private long hitTimer;

	// CONSTRUCTOR
	public Enemy(int type, int rank) {
		this.type = type;
		this.rank = rank;

		if (type == 1) {
			if (rank == 1) {
				color1 = new Color(0, 0, 255, 128);
				health = 1;
				w = 50;
				h = 20;
			}
			if (rank == 2) {
				color1 = new Color(150, 0, 255, 128);
				health = 2;
				w = 50;
				h = 20;
			}
		}

		dead = false;
		hit = false;
		hitTimer = 0;
	}

	// FUNCTIONS
	public int getx() {
		return x;
	}

	public int getw() {
		return w;
	}

	public int getr() {
		return r;
	}

	public int gety() {
		return y;
	}

	public int geth() {
		return h;
	}

	public int getType() {
		return type;
	}

	public int getRank() {
		return rank;
	}

	public boolean isDead() {
		return dead;
	}

	public void hit() {
		health--;
		if (health <= 0) {
			dead = true;
		}
		hit = true;
		hitTimer = System.nanoTime();
	}

	public void update() {
		if (hit) {
			long elapsed = (System.nanoTime() - hitTimer) / 1000000;
			if (elapsed > 50) {
				hit = false;
				hitTimer = 0;
			}
		}
	}

	public void draw(Graphics2D g) {

		if (hit) {
			g.setColor(Color.WHITE);
			g.fillRect(x, y, w, h);
			g.setStroke(new BasicStroke(3));
			g.setColor(Color.WHITE.darker());
			g.drawRect(x, y, w, h);
			g.setStroke(new BasicStroke(1));

		} else {
			g.setColor(color1);
			g.fillRect(x, y, w, h);
			g.setStroke(new BasicStroke(3));
			g.setColor(color1.darker());
			g.drawRect(x, y, w, h);
			g.setStroke(new BasicStroke(1));
		}
	}
}
