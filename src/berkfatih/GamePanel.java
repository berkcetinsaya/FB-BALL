package berkfatih;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import javax.swing.*;

public class GamePanel extends JPanel implements Runnable, KeyListener, MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;

	// FIELDS
	public static int WIDTH = 530;
	public static int HEIGHT = 530;

	private Thread thread;
	private boolean running;

	private BufferedImage image;
	private Graphics2D g;

	private int FPS = 59;

	public static Player player;
	public static Player2 player2;

	public static ArrayList<Bullet> bullets;
	public static ArrayList<Enemy> enemies;
	public static ArrayList<Ball> balls;
	public static ArrayList<PowerUp> powerups;
	public static ArrayList<Text> texts;

	private long levelStartTimer;
	private long levelStartTimerDiff;
	private int levelNumber;
	private boolean levelStart;
	private int levelDelay = 2000;

	private long slowDownTimer;
	private long slowDownTimerDiff;
	private int slowDownLength = 6000;

	private long speedUpTimer;
	private long speedUpTimerDiff;
	private int speedUpLength = 6000;

	private boolean escape = false;
	private boolean waiting = false;
	public static boolean godMode = false;
	public static boolean icindenGecmek = false;

	public static ServerSocket serverSocket;
	public static Socket socket;
	public static DataOutputStream out;
	public static DataInputStream in;
	public static String IPA;

	public static Server sv;
	public static Client cl;

	static Sound s1 = new Sound(new File("../../res/arctic_monkey.wav"));
	static Sound s2 = new Sound(new File("../../res/applause.wav"));
	static Sound s3 = new Sound(new File("../../res/win.wav"));
	static Sound s4 = new Sound(new File("../../res/ea.wav"));
	static Sound s5 = new Sound(new File("../../res/break.wav"));
	static Sound s6 = new Sound(new File("../../res/player_ball.wav"));
	static Sound s7 = new Sound(new File("../../res/ball_enemy.wav"));
	static Sound s8 = new Sound(new File("../../res/dead.wav"));
	static Sound s9 = new Sound(new File("../../res/heal.wav"));
	static Sound s10 = new Sound(new File("../../res/lose_life.wav"));
	static Sound s11 = new Sound(new File("../../res/fire_in_the_hole.wav"));
	static Sound s12 = new Sound(new File("../../res/sparta.wav"));
	static Sound s13 = new Sound(new File("../../res/fast.wav"));
	static Sound s14 = new Sound(new File("../../res/skype.wav"));
	static Sound s15 = new Sound(new File("../../res/trol.wav"));
	static Sound s16 = new Sound(new File("../../res/monsterkill.wav"));

	// CONSTRUCTOR
	public GamePanel() {
		super();
		waiting = true;

		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();

	}

	// FUNCTIONS
	public void addNotify() {
		super.addNotify();
		if (thread == null) {
			thread = new Thread(this);
			thread.start();

		}
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	public void socketSending() {
		try {
			out.writeInt(player.x);
			player2.x = in.readInt();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void Server() throws Exception, SecurityException {
		serverSocket = new ServerSocket(7777);
		InetAddress addr = InetAddress.getLocalHost();
		JOptionPane.showMessageDialog(null, "Your IP adress is: " + addr.getHostAddress() + "\nWaiting for other opponent");
		socket = serverSocket.accept();
		in = new DataInputStream(socket.getInputStream());
		out = new DataOutputStream(socket.getOutputStream());
		if (socket != null) {
			Game.game.setVisible(true);
			JOptionPane.showMessageDialog(Entrance.frames, "Play/Pause: P\nExit: Esc\nYou can play by arrows or mouse\nFire:Space\nPowerups are misteries :)");
		}
	}
	public static void Client(String IP) throws Exception, SecurityException {
		socket = new Socket(IP, 7777);
		in = new DataInputStream(socket.getInputStream());
		out = new DataOutputStream(socket.getOutputStream());
		if (socket != null) {
			Game.game.setVisible(true);
			JOptionPane.showMessageDialog(Entrance.frames, "Play/Pause: P\nExit: Esc\nYou can play by arrows or mouse\nFire:Space\nPowerups are misteries :)");
		}
	}

	@Override
	public void run() {

		running = true;

		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		player = new Player();
		player2 = new Player2();

		bullets = new ArrayList<Bullet>();
		enemies = new ArrayList<Enemy>();
		balls = new ArrayList<Ball>();
		powerups = new ArrayList<PowerUp>();
		texts = new ArrayList<Text>();

		levelStartTimer = 0;
		levelStartTimerDiff = 0;
		levelStart = true;
		levelNumber = 0;

		long startTime;
		long URDTimeMillis;
		long waitTime;
		long totalTime = 0;

		int frameCount = 0;
		int maxFrameCount = 30;

		long targetTime = 1000 / FPS;

		// GAME LOOP
		while (running) {
			startTime = System.nanoTime();
			if (socket != null) {
				socketSending();
			}
			gameUpdate();
			gameRender();
			gameDraw();

			URDTimeMillis = (System.nanoTime() - startTime) / 1000000;
			waitTime = targetTime - URDTimeMillis;

			try {
				Thread.sleep(waitTime);
			}
			catch (Exception e) {

			}
			while (waiting) {
				try {
					Thread.sleep(10);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			totalTime += System.nanoTime() - startTime;
			frameCount++;
			if (frameCount == maxFrameCount) {
				frameCount = 0;
				totalTime = 0;
			}
		}

		if (player.isDead() || escape) {
			g.setColor(new Color(100, 100, 255));
			g.fillRect(0, 0, WIDTH, HEIGHT);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", Font.PLAIN, 16));
			String s = "G A M E   O V E R";
			int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
			g.drawString(s, (WIDTH - length) / 2, HEIGHT / 2);
			s = "Final Score: " + player.getScore();
			try {
				player.writeFile();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
			g.drawString(s, (WIDTH - length) / 2, HEIGHT / 2 + 20);

			s8.start();
			String file_name = "Scores.txt";
			try {
				ReadFile file = new ReadFile(file_name);
				String[] aryLines = file.OpenFile();
				int i;

				for (i = 0; i < aryLines.length; i++) {
					System.out.println(aryLines[i]);
					length = (int) g.getFontMetrics().getStringBounds(aryLines[i], g).getWidth();
					g.drawString((aryLines[i]), (WIDTH - length) / 2, (i * 20) + HEIGHT / 2 + 40);
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			gameDraw();
		}
		else {
			g.setColor(new Color(0, 100, 255));
			g.fillRect(0, 0, WIDTH, HEIGHT);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", Font.PLAIN, 16));
			String s = "Y O U   W O N";
			int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
			g.drawString(s, (WIDTH - length) / 2, HEIGHT / 2);
			s = "Final Score: " + player.getScore();
			try {
				player.writeFile();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
			g.drawString(s, (WIDTH - length) / 2, HEIGHT / 2 + 20);

			String file_name = "Scores.txt";
			try {
				ReadFile file = new ReadFile(file_name);
				String[] aryLines = file.OpenFile();

				// int [] scores = new int [aryLines.length];
				// String [] array = new String[aryLines.length];
				/*
				 * for (int k = 0; k < aryLines.length; k++ ) { String [] arrayNew = aryLines[k].split(":"); scores[k] = Integer.parseInt(arrayNew[2]); int
				 * index = arrayNew[1].indexOf("Score"); array[k] = arrayNew[1].substring(0,index); }
				 */
				// Arrays.sort(scores);

				int i;
				for (i = 0; i < aryLines.length; i++) {
					System.out.println(aryLines[i]);
					length = (int) g.getFontMetrics().getStringBounds(aryLines[i], g).getWidth();
					g.drawString((aryLines[i]), (WIDTH - length) / 2, (i * 20) + HEIGHT / 2 + 40);
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			gameDraw();
		}
	}

	private void gameUpdate() {
		// new level
		if (levelStartTimer == 0 && enemies.size() == 0) {
			levelNumber++;
			levelStart = false;
			levelStartTimer = System.nanoTime();
		}
		else {
			levelStartTimerDiff = (System.nanoTime() - levelStartTimer) / 1000000;
			if (levelStartTimerDiff > levelDelay) {
				levelStart = true;
				levelStartTimer = 0;
				levelStartTimerDiff = 0;
			}
		}

		// create enemies
		if (levelStart && enemies.size() == 0) {
			createNewEnemies();
		}

		// player update
		player.update();
		player2.update();

		// bullet update
		for (int i = 0; i < bullets.size(); i++) {
			boolean remove = bullets.get(i).update();
			if (remove) {
				bullets.remove(i);
				i--;
			}
		}

		// ball update
		for (int i = 0; i < balls.size(); i++) {
			balls.get(i).update();
		}

		// enemy update
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).update();
		}

		// powerup update
		for (int i = 0; i < powerups.size(); i++) {
			boolean remove = powerups.get(i).update();
			if (remove) {
				powerups.remove(i);
				i--;
			}
		}
		// slowdown update
		if (slowDownTimer != 0) {
			slowDownTimerDiff = (System.nanoTime() - slowDownTimer) / 1000000;
			if (slowDownTimerDiff > slowDownLength) {
				slowDownTimer = 0;
				for (int j = 0; j < balls.size(); j++) {
					balls.get(j).setSlow(false);
				}
			}
		}
		// speedup update
		if (speedUpTimer != 0) {
			speedUpTimerDiff = (System.nanoTime() - speedUpTimer) / 1000000;
			if (speedUpTimerDiff > speedUpLength) {
				speedUpTimer = 0;
				for (int j = 0; j < balls.size(); j++) {
					balls.get(j).setFast(false);
				}
			}
		}
		// text update
		for (int i = 0; i < texts.size(); i++) {
			boolean remove = texts.get(i).update();
			if (remove) {
				texts.remove(i);
				i--;
			}
		}
		// ////// bullet-enemy collision
		for (int i = 0; i < bullets.size(); i++) {
			Bullet b = bullets.get(i);
			double bx = b.getx();
			double by = b.gety();
			for (int j = 0; j < enemies.size(); j++) {
				Enemy e = enemies.get(j);
				double ex = e.getx();
				double ey = e.gety();
				double ew = e.getw();
				double eh = e.geth();
				if (ex < bx && bx < ex + ew && by <= ey + eh) {
					e.hit();
					if (!icindenGecmek) {
						bullets.remove(i);
						i--;
					}
					break;
				}
			}
		}

		// //////// ball-enemy collision
		for (int i = 0; i < balls.size(); i++) {
			Ball b = balls.get(i);
			double bx = b.getx();
			double by = b.gety();
			double br = b.getr();
			for (int j = 0; j < enemies.size(); j++) {
				Enemy e = enemies.get(j);
				double ex = e.getx();
				double ey = e.gety();
				double eh = e.geth();
				double ew = e.getw();

				if (ex <= bx && bx + 2 * br <= ex + ew && ey - eh <= by && by <= ey + eh) {
					e.hit();
					if (!icindenGecmek) {
						b.dy = b.dy * (-1);
					}
					s7.start();
					break;
				}
				else if (ex <= bx && bx + 2 * br <= ex + ew && ey <= by + 2 * br && by + br * 2 <= ey) {
					e.hit();
					if (!icindenGecmek) {
						b.dy = b.dy * (-1);
					}
					s7.start();
					break;
				}
				else if (ey <= by + 2 * br && by + 2 * br <= ey + eh && ex <= bx + 2 * br && bx + 2 * br <= ex) {
					e.hit();
					if (!icindenGecmek) {
						b.dx = b.dx * (-1);
					}
					s7.start();
					break;
				}
				else if (ey <= by + 2 * br && by + 2 * br <= ey + eh && ex + ew <= bx && bx <= ex + ew) {
					e.hit();
					if (!icindenGecmek) {
						b.dx = b.dx * (-1);
					}
					s7.start();
					break;
				}
			}
		}

		// player - ball collision
		int px1 = player.getx();
		int py1 = player.gety();
		int pw1 = player.getw();
		for (int i = 0; i < balls.size(); i++) {
			Ball b = balls.get(i);
			double bx = b.getx();
			double by = b.gety();
			double br = b.getr();
			if (px1 <= bx && bx + 2 * br <= px1 + pw1 && py1 <= by + 2 * br) {
				b.dy = -b.dy;
				s6.start();
			}
			else if (by > HEIGHT + 20) {
				player.setFireFlag(false);
				powerups.clear();
				bullets.clear();
				balls.clear();
				player.loseLife();
				slowDownTimer = 0;
				speedUpTimer = 0;
				icindenGecmek = false;
				player.w = 100;
				player.x = GamePanel.WIDTH / 2;
				Ball b1 = new Ball();
				balls.add(b1);
				if (!player.isDead()) waiting = true;
			}
			else if (godMode == true && by > HEIGHT - 28) {
				player.x = (int) bx - (player.w / 2);
				s6.start();
			}
		}

		// player - powerup collision
		int px = player.getx();
		int py = player.gety();
		int pw = player.getw();
		for (int i = 0; i < powerups.size(); i++) {
			PowerUp p = powerups.get(i);
			double x = p.getx();
			double y = p.gety();
			double r = p.getr();
			// collected
			if (px <= x && x + 2 * r <= px + pw && py <= y + 2 * r && y + r * 2 <= py) {
				int type = p.getType();
				if (type == 1) {
					s9.start();
					player.gainLife();
					texts.add(new Text(player.getx() + 53, player.gety() - 20, 2000, "Extra Life"));
				}
				if (type == 2) {
					if (!player.isRecovering()) player.loseLife();
					s10.start();
					texts.add(new Text(player.getx() + 53, player.gety() - 20, 2000, "Lose Life"));
				}
				if (type == 3) {
					player.setFireFlag(true);
					s11.start();
					texts.add(new Text(player.getx() + 53, player.gety() - 20, 2000, "Fire in the hole"));
				}
				if (type == 4) {
					slowDownTimer = System.nanoTime();
					for (int j = 0; j < balls.size(); j++) {
						s12.start();
						balls.get(j).setFast(false);
						balls.get(j).setSlow(true);
					}
					texts.add(new Text(player.getx() + 53, player.gety() - 20, 2000, "Slow Down"));
				}
				if (type == 5) {
					speedUpTimer = System.nanoTime();
					for (int j = 0; j < balls.size(); j++) {
						s13.start();
						balls.get(j).setSlow(false);
						balls.get(j).setFast(true);
					}
					texts.add(new Text(player.getx() + 53, player.gety() - 20, 2000, "Speed Up"));
				}
				if (type == 6) {
					s16.start();
					if (player.getw() != 300) {
						player.w += 50;
					}
					texts.add(new Text(player.getx() + 53, player.gety() - 20, 2000, "Width+"));
				}
				if (type == 7) {
					s16.start();
					if (player.getw() != 100) {
						player.w -= 50;
					}
					texts.add(new Text(player.getx() + 53, player.gety() - 20, 2000, "Width-"));
				}
				if (type == 8) {
					s16.start();
					icindenGecmek = true;
					texts.add(new Text(player.getx() + 53, player.gety() - 20, 2000, "Icinden Gecmek"));
				}
				powerups.remove(i);
				i--;
			}
		}

		// check dead enemies
		for (int i = 0; i < enemies.size(); i++) {

			if (enemies.get(i).isDead()) {

				Enemy e = enemies.get(i);

				// powerups
				double rand = Math.random();
				if (rand < 0.020) powerups.add(new PowerUp(1, e.getx() + 10, e.gety() + 10)); // life
				else if (rand < 0.080) powerups.add(new PowerUp(2, e.getx() + 10, e.gety() + 10)); // lose
				else if (rand < 0.140) powerups.add(new PowerUp(3, e.getx() + 10, e.gety() + 10)); // fire
				else if (rand < 0.200) powerups.add(new PowerUp(4, e.getx() + 10, e.gety() + 10)); // slow
				else if (rand < 0.260) powerups.add(new PowerUp(5, e.getx() + 10, e.gety() + 10)); // speed
				else if (rand < 0.320) powerups.add(new PowerUp(6, e.getx() + 10, e.gety() + 10)); // width+
				else if (rand < 0.380) powerups.add(new PowerUp(7, e.getx() + 10, e.gety() + 10)); // width-
				else if (rand < 0.440) powerups.add(new PowerUp(8, e.getx() + 10, e.gety() + 10)); // icinden gecmek

				player.addScore(e.getRank() * 10 + e.getType() * 10);
				enemies.remove(i);
				s5.start();
				i--;
			}
		}

		// check dead player
		if (player.isDead()) {
			running = false;
		}
	}
	private void gameRender() {

		// Draw background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		// draw slowdown screen
		if (slowDownTimer != 0) {
			g.setColor(new Color(255, 255, 255, 64));
			g.fillRect(0, 0, WIDTH, HEIGHT);
		}

		// draw speedup screen
		if (speedUpTimer != 0) {
			g.setColor(new Color(255, 255, 255, 64));
			g.fillRect(0, 0, WIDTH, HEIGHT);
		}
		// draw player
		player.draw(g);
		player2.draw(g);

		// draw bullet
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).draw(g);
		}

		// draw enemy
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);
		}
		// draw ball
		for (int i = 0; i < balls.size(); i++) {
			balls.get(i).draw(g);
		}
		// draw powerups
		for (int i = 0; i < powerups.size(); i++) {
			powerups.get(i).draw(g);
		}

		// draw text
		for (int i = 0; i < texts.size(); i++) {
			texts.get(i).draw(g);
		}

		// draw level number
		if (levelStartTimer != 0) {
			if (!godMode) {
				player.setFireFlag(false);
			}
			player.setFiring(false);
			powerups.clear();
			bullets.clear();
			balls.clear();
			slowDownTimer = 0;
			speedUpTimer = 0;
			if (!godMode) {
				icindenGecmek = false;
			}
			if (levelNumber != 6) {
				g.setFont(new Font("Arial", Font.PLAIN, 18));
				String s = "-  L E V E L   " + levelNumber + "  -";
				int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
				int alpha = (int) (255 * Math.sin(3.14 * levelStartTimerDiff / levelDelay));
				if (alpha > 255) alpha = 255;
				g.setColor(new Color(255, 255, 255, alpha));
				g.drawString(s, WIDTH / 2 - length / 2, HEIGHT / 2);
			}
		}

		// draw player lives
		for (int i = 0; i < player.getLives(); i++) {
			g.setColor(Color.WHITE);
			g.fillRect(20 * i + 20, (20), 10, 3);
			g.setStroke(new BasicStroke(3));
			g.setColor(Color.WHITE.darker());
			g.fillRect(20 * i + 20, (20), 10, 3);
			g.setStroke(new BasicStroke(1));
		}

		// draw player score
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.PLAIN, 18));
		g.drawString("Score: " + player.getScore(), WIDTH - 150, 30);

		if (godMode) {
			g.setFont(new Font("Arial", Font.PLAIN, 18));
			String s = "-  G O D M O D E  -";
			int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
			g.setColor(new Color(255, 255, 255, 100));
			g.drawString(s, WIDTH / 2 - length / 2, HEIGHT - 20);
		}

		// draw slowdown meter
		if (slowDownTimer != 0) {
			g.setColor(Color.WHITE);
			g.drawRect(20, 60, 100, 8);
			g.fillRect(20, 60, (int) (100 - 100.0 * slowDownTimerDiff / slowDownLength), 8);
		}
		if (speedUpTimer != 0) {
			g.setColor(Color.WHITE);
			g.drawRect(20, 60, 100, 8);
			g.fillRect(20, 60, (int) (100 - 100.0 * speedUpTimerDiff / speedUpLength), 8);
		}

	}

	private void gameDraw() {
		Graphics g2 = this.getGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
	}

	public void mouseMoved(MouseEvent arg0) {
		player.x = arg0.getX();
		player.draw(g);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		int keyCode = arg0.getKeyCode();
		if (keyCode == KeyEvent.VK_SPACE) {
			player.setFiring(true);
		}
		if (keyCode == KeyEvent.VK_LEFT) {
			player.setLeft(true);
		}
		if (keyCode == KeyEvent.VK_RIGHT) {
			player.setRight(true);
		}
		if (keyCode == KeyEvent.VK_ESCAPE) {
			if (running) {
				running = false;
				escape = true;
			}
		}
		if (keyCode == KeyEvent.VK_F) {
			if (godMode) {
				player.setFiring(true);
			}
		}
		if (keyCode == KeyEvent.VK_F && ((arg0.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
			if (godMode) {
				player.setFiring(false);
			}
		}
		if (keyCode == KeyEvent.VK_G) {
			if (godMode) {
				godMode = false;
				player.setFiring(false);
				player.fireFlag = false;
				icindenGecmek = false;
				player.w = 100;
				s15.stop();
			}
			else {
				s15.start();
				godMode = true;
				player.fireFlag = true;
				icindenGecmek = true;
				player.lives = 99;
				player.addScore(99999999);
				player.w = 200;
			}
		}
		if (keyCode == KeyEvent.VK_P) {
			if (!waiting) {
				waiting = true;
				JOptionPane.showMessageDialog(null, "Play/Pause: P\nExit: Esc\nYou can play by arrows or mouse\nFire:Space\nPowerups are misteries :)");
			}
			else waiting = false;
		}
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
		int keyCode = arg0.getKeyCode();
		if (keyCode == KeyEvent.VK_SPACE) {
			player.setFiring(false);
		}
		if (keyCode == KeyEvent.VK_LEFT) {
			player.setLeft(false);
		}
		if (keyCode == KeyEvent.VK_RIGHT) {
			player.setRight(false);
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

	private void createNewEnemies() {
		enemies.clear();
		if (levelNumber == 1) {
			for (int i = 0; i < 10; i++) {
				Enemy e2 = new Enemy(1, 1);
				e2.x = 53;
				e2.y = (i * 20) + 90;
				for (int j = 0; j < i; j++) {
					Enemy e1 = new Enemy(1, 1);
					e1.x = j * 53;
					e1.y = e2.y;
					enemies.add(e1);
				}
			}
			Ball b0 = new Ball();
			balls.add(b0);
		}
		if (levelNumber == 2) {
			s2.start();
			for (int i = 1; i < 9; i++) {
				Enemy e1 = new Enemy(1, 1);
				e1.x = i * 53;
				e1.y = 90;
				enemies.add(e1);
			}
			for (int i = 1; i < 9; i++) {
				Enemy e1 = new Enemy(1, 2);
				e1.x = i * 53;
				e1.y = 110;
				enemies.add(e1);
			}
			for (int i = 1; i < 9; i++) {
				Enemy e1 = new Enemy(1, 1);
				e1.x = i * 53;
				e1.y = 130;
				enemies.add(e1);
			}
			for (int i = 1; i < 9; i++) {
				Enemy e1 = new Enemy(1, 2);
				e1.x = i * 53;
				e1.y = 150;
				enemies.add(e1);
			}
			for (int i = 2; i < 8; i++) {
				Enemy e1 = new Enemy(1, 1);
				e1.x = i * 53;
				e1.y = 170;
				enemies.add(e1);
			}
			for (int i = 3; i < 7; i++) {
				Enemy e1 = new Enemy(1, 2);
				e1.x = i * 53;
				e1.y = 190;
				enemies.add(e1);
			}
			for (int i = 4; i < 6; i++) {
				Enemy e1 = new Enemy(1, 1);
				e1.x = i * 53;
				e1.y = 210;
				enemies.add(e1);
			}
			Ball b0 = new Ball();
			balls.add(b0);
		}
		if (levelNumber == 3) {
			s2.start();
			for (int i = 1; i < 9; i++) {
				Enemy e2 = new Enemy(1, 2);
				e2.x = 53;
				e2.y = (i * 20) + 90;
				enemies.add(e2);
				for (int j = 2; j < 9; j++) {
					Enemy e1 = new Enemy(1, 1);
					e1.x = j * 53;
					e1.y = e2.y;
					enemies.add(e1);
				}
			}
			Ball b0 = new Ball();
			balls.add(b0);
		}
		if (levelNumber == 4) {
			s2.start();
			for (int i = 1; i < 9; i++) {
				Enemy e2 = new Enemy(1, 1);
				e2.x = 53;
				e2.y = (i * 20) + 90;
				enemies.add(e2);
				for (int j = 2; j < 5; j++) {
					Enemy e1 = new Enemy(1, 1);
					e1.x = j * 53;
					e1.y = e2.y;
					enemies.add(e1);
					for (int k = 5; k < 9; k++) {
						Enemy e3 = new Enemy(1, 1);
						e3.x = k * 53;
						e3.y = e1.y;
						enemies.add(e3);
					}
				}
			}
			Ball b0 = new Ball();
			balls.add(b0);

		}
		if (levelNumber == 5) {
			s2.start();
			for (int i = 1; i < 9; i++) {
				Enemy e2 = new Enemy(1, 2);
				e2.x = 53;
				e2.y = (i * 20) + 90;
				enemies.add(e2);
				for (int j = 2; j < 5; j++) {
					Enemy e1 = new Enemy(1, 2);
					e1.x = j * 53;
					e1.y = e2.y;
					enemies.add(e1);
					for (int k = 5; k < 9; k++) {
						Enemy e3 = new Enemy(1, 2);
						e3.x = k * 53;
						e3.y = e1.y;
						enemies.add(e3);
					}
				}
			}
			Ball b0 = new Ball();
			balls.add(b0);
		}
		if (levelNumber == 6) {
			s3.start();
			running = false;
		}
	}

}
