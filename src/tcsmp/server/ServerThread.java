package tcsmp.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import tcsmp.exceptions.RegistrationException;

public class ServerThread extends Thread {

	private String email;
	private Server server;

	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;

	public ServerThread(Socket link, Server server) {
		this.socket = link;
		this.server = server;

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

//			email = tokens[1] + "@" + server.getDomainName();
			email = tokens[1];
			out.writeUTF("REGISTRATION OK - " + email);

			do {
				message_in = in.readUTF();
				tokens = message_in.split(":", 4);

				if (message_in.equals("END")) {
					System.out.println("Bye bye!");
					break;
				} else if (tokens[0].equals("Broadcast")) {
					String bc = tokens[1];
					for (Socket client : server.getClientsSockets()) {
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
					for (ServerThread c : server.getServerThreads()) {
						if (c.email.equals(dest)) {
							found = true;
							c.out.writeUTF(
									"Message from " + email + " :\n" + "Subject: " + tokens[2] + "\n" + tokens[3]);
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
		server.getClientsSockets().remove(this.socket);
		try {
			socket.close();
		} catch (IOException ex) {
			Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
