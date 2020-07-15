package tcsmp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	private static ServerSocket serverSocket;
	private static Socket link;
	private static final int PORT = 1234;

	public static ArrayList<ServerThread> serverThreads = new ArrayList<ServerThread>();

	public static void main(String[] args) {

		ArrayList<Socket> clientsSockets = new ArrayList<Socket>();
		try {
			serverSocket = new ServerSocket(PORT);
		} catch (IOException ex) {
			System.out.println("Unable to connect to this port");
		}

		try {
			while (true) {
				link = serverSocket.accept();
				ServerThread serverThread = new ServerThread(link, clientsSockets);
				serverThreads.add(serverThread);
				clientsSockets.add(link);

				serverThread.start();

				System.out.println("Clients # = " + clientsSockets.size());
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
