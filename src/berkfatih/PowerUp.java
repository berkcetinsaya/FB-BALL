package berkfatih;

import java.awt.*;

public class PowerUp {

	// FIELDS
	private int x;
	private int y;
	private int r;

	private int type;
	private Color color1;

	// 1 -- +1 life
	// 2 -- -1 life
	// 3 -- Fire
	// 4 -- slow down time
	// 5 -- speed up
	// 6 -- player width+
	// 7 -- player width-
	// 8 -- icinden gecmek
	
	// CONSTRUCTOR
	public PowerUp(int type, int x, int y) {
		this.type = type;
		this.x = x;
		this.y = y;
		if (type == 1) {
			color1 = Color.PINK;
			r = 5;
		}
		if (type == 2) {
			color1 = Color.YELLOW;
			r = 10;
		}
		if (type == 3) {
			color1 = Color.RED;
			r = 5;
		}
		if (type == 4) {
			color1 = Color.WHITE;
			r = 5;
		}
		if (type == 5) {
			color1 = Color.BLUE;
			r = 10;
		}
		if(type == 6){
			color1 = Color.GREEN;
			r = 5;
		}
		if(type == 7){
			color1 = Color.CYAN;
			r = 10;
		}
		if(type == 8){
			color1 = Color.MAGENTA;
			r = 5;
		}
	}

	// FUNCTIONS
	public double getx() {
		return x;
	}

	public double gety() {
		return y;
	}

	public double getr() {
		return r;
	}

	public int getType() {
		return type;
	}

	public boolean update() {

		y += 5;
		if (y > GamePanel.HEIGHT + r) {
			return true;
		}
		return false;
	}

	public void draw(Graphics2D g) {

		g.setColor(color1);
		g.fillRect((int) (x + r), (int) (y - r), 2 * r, 2 * r);
		g.setStroke(new BasicStroke(3));
		g.setColor(color1.darker());
		g.fillRect((int) (x + r), (int) (y - r), 2 * r, 2 * r);
		g.setStroke(new BasicStroke(1));
	}
}
