package berkfatih;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Client extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextField fieldEnter;
	private JTextArea areaDisplay;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Socket client;
	private String chatServer, message = "";

	public Client(String host) {
		super("Client");
		fieldEnter = new JTextField();
		fieldEnter.setEditable(false);
		fieldEnter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendData(e.getActionCommand());
				fieldEnter.setText("");
			}
		});
		add(fieldEnter, BorderLayout.NORTH);
		areaDisplay = new JTextArea();
		add(new JScrollPane(areaDisplay), BorderLayout.CENTER);
		setSize(300, 150);
		setVisible(true);
	}
	private void processConnection() throws IOException {

		setTextFieldEditable(true);
		do {
			try {
				message = (String) input.readObject();
				displayMessage("\n" + message);
			}
			catch (ClassNotFoundException e) {
				displayMessage("\nUnknown Object Type Received");
			}
		} while (!message.equals("SERVER>>> TERMINATE"));
	}

	private void displayMessage(final String messageToDisp) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				areaDisplay.append(messageToDisp);
			}
		});
	}
	private void connectToServer() throws IOException {
		displayMessage("\nAttempting for connection\n");
		client = new Socket(InetAddress.getByName(chatServer), 7776);
		displayMessage("\nConnection to " + client.getInetAddress().getHostName());
	}
	public void runClient() {
		try {
			connectToServer();
			getStream();
			processConnection();
		}
		catch (EOFException eofException) {
			displayMessage("\nClient Terminated Connection");
		}
		catch (IOException e2) {
			e2.printStackTrace();
		}
		finally {
			closeConnection();
		}
	}
	private void getStream() throws IOException {
		output = new ObjectOutputStream(client.getOutputStream());
		output.flush();
		input = new ObjectInputStream(client.getInputStream());
		displayMessage("\nGot I/O streams\n");
	}
	private void closeConnection() {
		displayMessage("\nTerminated");
		setTextFieldEditable(false);
		try {
			output.close();
			input.close();
			client.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void setTextFieldEditable(final boolean b) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				fieldEnter.setEditable(b);
			}
		});
	}
	private void sendData(String message) {
		try {
			output.writeObject("CLIENT>>> " + message);
			output.flush();
			displayMessage("\nCLIENT>>> " + message);
		}
		catch (IOException e) {
			areaDisplay.append("\nError writing object");
		}
	}
}
