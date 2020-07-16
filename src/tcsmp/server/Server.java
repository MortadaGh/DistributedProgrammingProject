package tcsmp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
	private static ServerSocket serverSocket;
	private static Socket link;
	private static final int PORT = 1234;
	private static String domainName;
	
	public static ArrayList<ServerThread> serverThreads = new ArrayList<ServerThread>();

	public static void main(String[] args) {

		ArrayList<Socket> clientsSockets = new ArrayList<Socket>();
		try {
			serverSocket = new ServerSocket(PORT);
		} catch (IOException ex) {
			System.out.println("Unable to connect to this port");
			System.exit(0);
		}
		
		Scanner in = new Scanner(System.in);
		final String DOMAIN_NAME_PATTERN = "^((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\.)+[A-Za-z]{2,6}$";
		System.out.print("Enter domain name: ");
		domainName = in.nextLine();
		if(!domainName.matches(DOMAIN_NAME_PATTERN)) {
			System.out.println("Invalid domain name");
			System.exit(0);
		}
		
		try {
			while (true) {
				link = serverSocket.accept();
				ServerThread serverThread = new ServerThread(link, clientsSockets, domainName);
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
