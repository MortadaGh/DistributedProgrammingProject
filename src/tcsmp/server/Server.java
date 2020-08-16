package tcsmp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tcsmp.utils.Email;

public class Server implements Runnable {

	private Integer port;
	private String domainName;
	private Boolean isStarted = false;
	private Thread currentThread;

	private ServerSocket serverSocket;
	private Socket link;

	private ArrayList<Socket> clientsSockets;
	private ArrayList<ServerThread> serverThreads;
	private HashMap<String, List<Email>> emails;

	public Server(String domainName, Integer port) {
		this.port = port;
		this.domainName = domainName;

		try {
			serverSocket = new ServerSocket(port);
			clientsSockets = new ArrayList<Socket>();
			serverThreads = new ArrayList<ServerThread>();
			emails = new HashMap<String, List<Email>>();
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
//		System.out.println("Server <" + domainName + "> running on port <" + port + "> ...");
		try {
			while (true) {
				link = serverSocket.accept();
				ServerThread serverThread = new ServerThread(link, this);
				serverThreads.add(serverThread);
				clientsSockets.add(link);
				serverThread.start();
				System.out.println("["+ domainName + "]: Clients number = " + clientsSockets.size());
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public Boolean getIsStarted() {
		return isStarted;
	}

	public void setIsStarted(Boolean isStarted) {
		this.isStarted = isStarted;
	}

	public Thread getCurrentThread() {
		return currentThread;
	}

	public void setCurrentThread(Thread currentThread) {
		this.currentThread = currentThread;
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public Socket getLink() {
		return link;
	}

	public void setLink(Socket link) {
		this.link = link;
	}

	public ArrayList<Socket> getClientsSockets() {
		return clientsSockets;
	}

	public void setClientsSockets(ArrayList<Socket> clientsSockets) {
		this.clientsSockets = clientsSockets;
	}

	public ArrayList<ServerThread> getServerThreads() {
		return serverThreads;
	}

	public void setServerThreads(ArrayList<ServerThread> serverThreads) {
		this.serverThreads = serverThreads;
	}

	public HashMap<String, List<Email>> getEmails() {
		return emails;
	}

	public void setEmails(HashMap<String, List<Email>> emails) {
		this.emails = emails;
	}
}
