package berkfatih;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.io.IOException;

public class Entrance extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	JButton single;
	JButton multi;
	JButton high;
	JButton exit;
	JButton chatButton;

	JPanel center;
	JPanel south;
	JPanel north;

	ImageIcon image;
	JLabel lbl;

	BorderLayout b;
	FlowLayout f;
	GridLayout g;

	static String name;
	static String ip;

	public static ChatThread chat;
	public static Server sv;
	public static Client cl;
	
	public static boolean multiplayer = false;

	public Entrance() {
		super("FB-BALL");

		GamePanel.s4.start();
		image = new ImageIcon("eric.png");

		b = new BorderLayout();
		setLayout(b);

		north = new JPanel();
		add(north, BorderLayout.NORTH);
		north();

		south = new JPanel();
		add(south, BorderLayout.SOUTH);
		south();

		center = new JPanel();
		add(center, BorderLayout.CENTER);
		center();
	}

	public void north() {
		f = new FlowLayout();
		north.setLayout(f);

		single = new JButton("Single Player");
		single.setForeground(Color.BLACK);
		single.setBackground(Color.WHITE);
		multi = new JButton("Multi Player");
		multi.setForeground(Color.BLACK);
		multi.setBackground(Color.WHITE);
		single.addActionListener(this);
		multi.addActionListener(this);
		north.add(single);
		north.add(multi);
	}

	public void south() {
		f = new FlowLayout();
		south.setLayout(f);
		chatButton = new JButton("Chat");
		chatButton.setForeground(Color.GREEN);
		chatButton.setBackground(Color.WHITE);
		high = new JButton("High Score");
		high.setForeground(Color.BLUE);
		high.setBackground(Color.WHITE);
		exit = new JButton("Exit");
		exit.setForeground(Color.RED);
		exit.setBackground(Color.WHITE);
		high.addActionListener(this);
		exit.addActionListener(this);
		chatButton.addActionListener(this);
		south.add(chatButton);
		south.add(high);
		south.add(exit);
	}

	public void center() {
		lbl = new JLabel(image, JLabel.CENTER);
		center.add(lbl);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == single) {
			singleplayer();
			GamePanel.s1.start();
		}
		else if (e.getSource() == multi) {
			multiplayer();
			multiplayer = true;
			GamePanel.s1.start();
		}
		else if (e.getSource() == chatButton) {
			chat = new ChatThread();
			chat.start();
		}
		else if (e.getSource() == high) {
			Runtime rt = Runtime.getRuntime();
			String url = "localhost";
			int ss = JOptionPane.showConfirmDialog(south, "You will redirect to our web server?", "High Score", JOptionPane.YES_NO_OPTION);
			if (ss == JOptionPane.YES_OPTION) {
				try {
					rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
				}
				catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		else if (e.getSource() == exit) {
			int ss = JOptionPane.showConfirmDialog(south, "Are You Sure?", "Exit", JOptionPane.YES_NO_OPTION);
			if (ss == JOptionPane.YES_OPTION) {
				System.exit(0);
			}
		}
		else {

		}
	}
	public static JFrame framem = new JFrame("Multi Player Mode");
	public void multiplayer() {
		this.setVisible(false);

		framem.setSize(350, 100);
		framem.setLocationRelativeTo(null);
		framem.setResizable(false);
		framem.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		framem.setVisible(true);
		JLabel label = new JLabel("NAME: ");
		JLabel lip = new JLabel("IP ADDRESS: ");
		JPanel panel = new JPanel();
		JPanel panel2 = new JPanel();
		JTextField text = new JTextField("Write Your Name Here");
		JTextField textip = new JTextField("Write Your IP Address Here");
		JButton mbtn = new JButton("Create");
		JButton mbtn1 = new JButton("Join");
		framem.add(panel, BorderLayout.NORTH);
		framem.add(panel2, BorderLayout.CENTER);
		panel.setLayout(f);
		panel.add(label);
		panel.add(text);
		panel2.add(lip);
		panel2.add(textip);
		panel2.add(mbtn);
		panel2.add(mbtn1);

		ActionListener buttonact = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				name = text.getText();
				ip = textip.getText();
				framem.setVisible(false);
				try {
					GamePanel.Server();
				}
				catch (SecurityException e1) {
					e1.printStackTrace();
				}
				catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		};
		ActionListener buttonact1 = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				name = text.getText();
				ip = textip.getText();
				framem.setVisible(false);
				try {
					//String choice = JOptionPane.showInputDialog("IP?");
					GamePanel.Client(ip);
				}
				catch (SecurityException e1) {
					e1.printStackTrace();
				}
				catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		};
		MouseListener clear = new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (arg0.getSource() == text) {
					text.setText("");
				}
				if (arg0.getSource() == textip) {
					textip.setText("");

				}
			}
		};
		mbtn.addActionListener(buttonact);
		mbtn1.addActionListener(buttonact1);
		text.addMouseListener(clear);
		textip.addMouseListener(clear);
	}
	public static JFrame frames = new JFrame("Single Player Mode");

	public void singleplayer() {
		this.setVisible(false);
		frames.setSize(300, 80);
		frames.setLocationRelativeTo(null);
		frames.setResizable(false);
		frames.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frames.setVisible(true);
		JLabel label = new JLabel("NAME: ");
		JPanel panel = new JPanel();
		JTextField text = new JTextField("Write Your Name Here");
		JButton sbtn = new JButton("START");
		frames.add(panel);
		panel.setLayout(f);
		panel.add(label);
		panel.add(text);
		panel.add(sbtn);
		ActionListener buttonact = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				name = text.getText();
				if (text.getText() != null) {
					Game.game.setVisible(true);
					frames.setVisible(false);
					JOptionPane.showMessageDialog(frames, "Play/Pause: P\nExit: Esc\nYou can play by arrows or mouse\nFire:Space\nPowerups are misteries :)");
				}
			}
		};
		MouseListener clear = new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {

			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (arg0.getSource() == text) {
					text.setText("");
				}
			}
		};
		sbtn.addActionListener(buttonact);
		text.addMouseListener(clear);

	}
}
