package berkfatih;

import javax.swing.JOptionPane;

public class ChatThread extends Thread {
	
	public static String choice = JOptionPane.showInputDialog("Create a new chat connection or Join via IP?(create/join)");
	public static Server sv;
	public static Client cl;

	public ChatThread() {
	}
	
	@Override
	public void run() {
		while (!(choice.equals("create") && choice.equals("join"))) {
			if (choice.equals("create")) {
				try {
					GamePanel.s14.start();
					sv = new Server();
					sv.runServer();
				}
				catch (Exception e) {
					System.out.println(e);
				}
				break;
			}
			else if (choice.equals("join")) {
				choice = JOptionPane.showInputDialog("Please enter the IP address you want to connect to");
				try {
					cl = new Client(choice);
					cl.runClient();
				}
				catch (Exception e) {
					System.out.println(e);
				}
				break;
			}
			else {
				choice = JOptionPane.showInputDialog("That is not a valid option.\nCreate a new chat connection or Join via IP?(create/join)");
			}
		}
	}
}
