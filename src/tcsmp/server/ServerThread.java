package tcsmp.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import tcsmp.exceptions.RegistrationException;

public class ServerThread extends Thread {

	private Socket socket;
	private ArrayList<Socket> clientsSockets;
	private String email;
	
	private DataInputStream in;
	private DataOutputStream out;
	private String domainName;
	
	public ServerThread(Socket socket, ArrayList<Socket> clientsSockets, String domainName) {
		this.socket = socket;
		this.clientsSockets = clientsSockets;
		this.domainName = domainName;
		
		try {
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void run() {
		String message_in;

		try {
			message_in = in.readUTF();
			String[] tokens = message_in.split(":", 2);

			if (!tokens[0].equals("Register")) {
				System.out.println("Registration not established!");
				out.writeUTF("Registration not established!");
				throw new RegistrationException();
			}

			email = tokens[1] + "@" + domainName;
			out.writeUTF("REGISTRATION OK - " + email);

			do {
				message_in = in.readUTF();
				tokens = message_in.split(":", 4);

				if (message_in.equals("END")) {
					System.out.println("Bye bye!");
					break;
				} else if (tokens[0].equals("Broadcast")) {
					String bc = tokens[1];
					for (Socket client : clientsSockets) {
						if (client.equals(socket)) {
							continue;
						}
						client.getOutputStream();
						DataOutputStream clientOut = new DataOutputStream(client.getOutputStream());
						clientOut.writeUTF(tokens[0] + "<" + email + "> : " + bc);
					}
				} else if (tokens[0].equals("Message")) {
					String dest = tokens[1];
					boolean found = false;
					for (ServerThread c : Server.serverThreads) {
						if (c.email.equals(dest)) {
							found = true;
							c.out.writeUTF("Message from " + email + " :\n" + "Subject: " + tokens[2] + "\n" + tokens[3]);
							break;
						}
					}
					if (!found) {
						out.writeUTF("Client not found");
					}
				}
			} while (true);

			closeconnection();

		} catch (EOFException ex1) {
			closeconnection();
//		} catch (SocketException e) {
//			closeconnection();
		} catch (RegistrationException e) {
			closeconnection();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void closeconnection() {
		clientsSockets.remove(this.socket);
		try {
			socket.close();
		} catch (IOException ex) {
			Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
