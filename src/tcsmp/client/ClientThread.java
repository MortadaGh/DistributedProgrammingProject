package tcsmp.client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

class ClientThread extends Thread {

	private Socket link;
	private DataInputStream in;

	public ClientThread(Socket link) {
		this.link = link;
		try {
			in = new DataInputStream(link.getInputStream());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			try {
				System.out.println(in.readUTF());
			} catch (SocketException ex) {
				break;
			} catch (IOException ex) {
				ex.printStackTrace();
				break;
			}
		}
	}
}
