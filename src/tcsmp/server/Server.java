package tcsmp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable {
	
	private Integer port;
	private String domainName;
	private Boolean isStarted = false;
	private Thread currentThread;
	
	private ServerSocket serverSocket;
	private Socket link;
	
	private ArrayList<Socket> clientsSockets;
	private ArrayList<ServerThread> serverThreads;

	public Server(String domainName, Integer port) {
		this.port = port;
		this.domainName = domainName;

		try {
			serverSocket = new ServerSocket(port);
			clientsSockets = new ArrayList<Socket>();
			serverThreads = new ArrayList<ServerThread>();
			isStarted = true;
			currentThread = new Thread(this);
		} catch (IOException ex) {
			isStarted = false;
			System.out.println("Unable to connect to this port");
//			System.exit(0);
		}
	}

	public void start() {
		currentThread.start();
	}
	
	public void run() {
		System.out.println("Server <" + domainName + "> running on port <" + port + "> ...");
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
