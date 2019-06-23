package berkfatih;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Server extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextField enterField;
	private JTextArea displayArea;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;
	private int counter = 1;

	public Server() {
		super("Server");
		enterField = new JTextField();
		enterField.setEditable(false);
		enterField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendData(e.getActionCommand());
				enterField.setText("");
			}
		});
		add(enterField, BorderLayout.NORTH);
		displayArea = new JTextArea();
		add(new JScrollPane(displayArea), BorderLayout.CENTER);
		setSize(300, 150);
		setVisible(true);
	}
	public void runServer() {
		try {
			server = new ServerSocket(7776);
			//while (true) {
				try {
					waitForConnection();
					getStream();
					processConnection();
				}
				catch (EOFException eofException) {
					displayMessage("\nServer Terminated Connection");
				}
				finally {
					closeConnection();
					++counter;
				}
			//}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void displayMessage(final String messageToDisp) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				displayArea.append(messageToDisp);
			}
		});
	}
	private void waitForConnection() throws IOException {
		displayMessage("Waiting for connection\n");
		InetAddress addr = InetAddress.getLocalHost();
		displayMessage("Your IP address is: " + addr.getHostAddress() + "\nWaiting for other opponent");
		
		try{
			connection = server.accept();	
		}catch(IOException e){
			e.printStackTrace();
		}
			
		displayMessage("\nConnection " + counter + " received from: " + connection.getInetAddress().getHostName());
	}
	private void getStream() throws IOException {
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		displayMessage("\nGot I/O streams\n");
	}
	private void processConnection() throws IOException {
		String message = "Connection Successful";
		sendData(message);
		setTextFieldEditable(true);
		do {
			try {
				message = (String) input.readObject();
				displayMessage("\n" + message);
			}
			catch (ClassNotFoundException e) {
				displayMessage("\nUnknown Object Type Received");
			}
		} while (!message.equals("CLIENT>>> TERMINATE"));
	}
	private void closeConnection() {
		displayMessage("\nTerminated\n");
		setTextFieldEditable(false);
		try {
			output.close();
			input.close();
			connection.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void setTextFieldEditable(final boolean b) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				enterField.setEditable(b);
			}
		});
	}
	private void sendData(String message) {
		try {
			output.writeObject("SERVER>>> " + message);
			output.flush();
			displayMessage("\nSERVER>>> " + message);
		}
		catch (IOException e) {
			displayArea.append("\nError writing object");
		}
	}
}