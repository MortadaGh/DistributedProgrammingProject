package tcsmp.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import tcsmp.exceptions.RegistrationException;
import tcsmp.utils.DataUtils;
import tcsmp.utils.Email;
import tcsmp.utils.HostPort;

public class ServerThread extends Thread {

	private String email;
	private Server server;

	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	private ObjectInputStream objectIn;
	private ObjectOutputStream objectOut;

	public ServerThread(Socket link, Server server) {
		this.socket = link;
		this.server = server;

		try {
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			objectOut = new ObjectOutputStream(socket.getOutputStream());
			objectIn = new ObjectInputStream(socket.getInputStream());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void run() {
		String message_in;
		Boolean isServer = false;

		try {
			message_in = in.readUTF();
//			String[] tokens = message_in.split(":", 2);
			String[] tokens = message_in.split(":");
			
			if (tokens[0].equals("Server-Domain")) {			
				email = tokens[2];
				isServer = true;
				out.writeUTF("Checking if " + tokens[1] + " exist...");
			}
			
			else if(!tokens[0].equals("Register")) {
				System.out.println("Registration not established!");
				out.writeUTF("Registration not established!");
				throw new RegistrationException();
			} else {
				email = tokens[1];
				out.writeUTF("REGISTRATION OK - " + email);	
				server.getEmails().put(email, new ArrayList<Email>());
			}
			System.out.println(email);

			email = tokens[1] + "@" + server.getDomainName();
//<<<<<<< HEAD
//			email = tokens[1];
//			out.writeUTF("REGISTRATION OK - " + email);
//			server.getEmails().put(email, new ArrayList<Email>());
//			
//			do {
//				message_in = in.readUTF();
//				tokens = message_in.split(":", 4);
//=======
//>>>>>>> 370e42926b648855ca5a4ee1f000c998431643e8

			do {	
				if(isServer == false) {
					message_in = in.readUTF();
					tokens = message_in.split(":", 4);	
				} else {
					tokens = tokens[3].split(";", 4);
					isServer = false;
				}
				
				if (message_in.equals("END")) {
					System.out.println("Bye bye!");
					break;
				} else if (tokens[0].equals("Message")) {
					Email emailObj = (Email) objectIn.readObject();
					System.out.println("Email = " + emailObj);
					String dest = tokens[1];
					
					server.getEmails().get(dest).add(emailObj);
					
					System.out.println("server.getEmails() = " + server.getEmails());
					
					boolean found = false;
					String[] destinationEmail = dest.split("@", 2);
					String[] hostEmail = email.split("@", 2);
					
					if(!destinationEmail[1].equals(hostEmail[1])) {
						String responseFromServer; 
						try {
							HostPort destinationHP = DataUtils.servers.get(destinationEmail[1]);
							HostPort hostPort = DataUtils.servers.get(hostEmail[1]);
							
							// Create new Socket with the destination host/port and then send the email to the destination server
							Socket socketServer = new Socket(destinationHP.getHost(), destinationHP.getPort());
							DataOutputStream serverOut = new DataOutputStream(socketServer.getOutputStream());
							System.out.println("Sending Email to " + dest + " AT port " + destinationHP.getPort());
							String message_out = "Server-Domain:"+ dest + ":" + email + ":" + String.join(";", tokens); 
							serverOut.writeUTF(message_out);
							out.writeUTF("Client not found trying to communicate with " + dest);

							DataInputStream serverIn = new DataInputStream(socketServer.getInputStream());
							responseFromServer = serverIn.readUTF();
							System.out.println("response " + responseFromServer);
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					} else {
					for (ServerThread c : server.getServerThreads()) {
						if (c.email.equals(dest)) {
							found = true;
//							c.out.writeUTF("Email = " + email);
//							c.out.writeUTF("Message from " + dest + " :\n" + "Subject: " + tokens[2] + "\n" + tokens[3]);
							break;
						}
						out.writeUTF("Email has been send...");
					}
					}
					if (!found) {
						System.out.println("Client not found");
					}
				}  else if (tokens[0].equals("Refresh")) {
					objectOut.writeUTF("Refresh");
					ArrayList<Email> emails = (ArrayList<Email>) server.getEmails().get(email);
					System.out.println("emails = " + emails);
					objectOut.writeObject(emails.clone());
//					objectOut.close();
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
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException");
			e.printStackTrace();
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
