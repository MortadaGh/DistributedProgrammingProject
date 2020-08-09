package tcsmp.client;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import tcsmp.utils.Email;

class ClientThread extends Thread {

	private Client client;
	private Socket link;
	private DataInputStream in;
	private ObjectInputStream objectIn;
//	private ObjectOutputStream objectOut;

	public ClientThread(Socket link, Client client) {
		this.link = link;
		this.client = client;
		try {
			in = new DataInputStream(link.getInputStream());
//		in = client.getIn();
//			objectOut = new ObjectOutputStream(link.getOutputStream());
//			objectIn = new ObjectInputStream(link.getInputStream());
			objectIn = client.getObjectIn();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			try {
				String message_in = objectIn.readUTF();
				if (message_in.equals("Refresh")) {
					ArrayList<Email> emails = (ArrayList<Email>) objectIn.readObject();
					System.out.println("emails = " + emails);
					client.setEmails(emails);
//					emails.forEach(email -> {
//						System.out.println(email);
//					});
				} else {
					System.out.println(message_in);
				}
			} catch (SocketException ex) {
				break;
			} catch (IOException ex) {
				ex.printStackTrace();
				break;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
