package berkfatih;

import java.awt.*;
import java.io.*;

public class Player {

	// FIELDS
	public int x;
	public int y;
	public int w = 100;
	private int h = 10;

	private int r = 50;

	private int dx;
	private int speed;

	private boolean left;
	private boolean right;

	private boolean firing;
	private long firingTimer;
	private long firingDelay;
	public boolean fireFlag;

	private boolean recovering;
	private long recoveryTimer;

	public int lives;
	private Color color1;
	private Color color2;
	
	private long score;

	// CONSTRUCTOR
	public Player() {
		x = GamePanel.WIDTH / 2;
		y = GamePanel.HEIGHT - h;

		dx = 0;
		speed = 10;

		lives = 3;
		color1 = Color.WHITE;
		color2 = Color.RED;

		firing = false;
		firingTimer = System.nanoTime();
		firingDelay = 200;
		fireFlag = false;

		recovering = false;
		recoveryTimer = 0;

		score = 0;
	}

	// FUNCTIONS
	public int getx() {
		return x;
	}

	public boolean isDead() {
		return lives <= 0;
	}

	public int gety() {
		return y;
	}

	public int getr() {
		return r;
	}

	public int getw() {
		return w;
	}

	public int geth() {
		return h;
	}

	public long getScore() {
		return score;
	}

	public int getLives() {
		return lives;
	}

	public boolean isRecovering() {
		return recovering;
	}

	public void setLeft(boolean b) {
		left = b;
	}

	public void setRight(boolean b) {
		right = b;
	}

	public void setFiring(boolean b) {
		firing = b;
	}
	public boolean getFiring() {
		
		return firing;
	}
	public void setFireFlag(boolean b) {
		fireFlag = b;
	}

	public void addScore(int i) {
		score += i;
	}

	public void gainLife() {
		lives++;
	}

	public void loseLife() {
		lives--;
		recovering = true;
		recoveryTimer = System.nanoTime();
	}

	public void writeFile() throws IOException {

		RandomAccessFile raf = new RandomAccessFile(new File("Scores.txt"), "rw");
		raf.seek(raf.length());
		raf.writeBytes(Entrance.name + "     Score :" + getScore() + "\n");
		raf.close();
	}
	
	public void update() {
		if (left) {
			dx = -speed;
		}
		if (right) {
			dx = speed;
		}
		x += dx;

		if (x < 0) x = 0;
		if (x > GamePanel.WIDTH - w) x = GamePanel.WIDTH - w;

		dx = 0;

		// firing
		if (fireFlag) {
			if (firing) {
				long elapsed = (System.nanoTime() - firingTimer) / 1000000;
				if (elapsed > firingDelay) {
					firingTimer = System.nanoTime();
					GamePanel.bullets.add(new Bullet(270, x+2, y));
					GamePanel.bullets.add(new Bullet(270, x + w-4, y));

				}
			}
		}
		if (recovering) {
			long elapsed = (System.nanoTime() - recoveryTimer) / 1000000;
			if (elapsed > 2000) {
				recovering = false;
				recoveryTimer = 0;
			}
		}
	}

	public void draw(Graphics2D g) {

		if (recovering) {
			g.setColor(color2);
			g.fillRect(x, y, w, h);
			g.setStroke(new BasicStroke(3));
			g.setColor(color2.darker());
			g.fillRect(x, y, w, h);
			g.setStroke(new BasicStroke(1));
		}
		else {
			g.setColor(color1);
			g.fillRect(x, y, w, h);
			g.setStroke(new BasicStroke(3));
			g.setColor(color1.darker());
			g.fillRect(x, y, w, h);
			g.setStroke(new BasicStroke(1));
		}
	}
}
