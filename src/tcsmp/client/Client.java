package tcsmp.client;

import static java.lang.System.exit;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import tcsmp.utils.Email;
import tcsmp.utils.HostPort;

public class Client {
	private Socket socket;
	private Integer port;
	private String host;
	private String email;

	private DataInputStream in;
	private DataOutputStream out;
	private ObjectInputStream objectIn;
	private ObjectOutputStream objectOut;
	
	private String message_in;
	private String message_out;

	private ArrayList<Email> emails;
	
	public Client(String email, HostPort hp) throws IOException {
		this.email = email;
		this.port = hp.getPort();
		this.host = hp.getHost();

		try {
			socket = new Socket(host, port);
			objectOut = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			objectIn = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
//			in = new DataInputStream(socket.getInputStream());
//			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException ex) {
			System.out.println("Host not reachable!");
			exit(0);
		}
	}

	public void start() throws IOException, ClassNotFoundException {
		Scanner input = new Scanner(System.in);

//		System.out.println("Please Enter your name:");
//		String name = input.nextLine();
//		String req = "Register:" + name;
		String req = "Register:" + email;

		objectOut.writeUTF(req);
		message_in = objectIn.readUTF();
		if (!message_in.startsWith("REGISTRATION OK")) {
			System.out.println(message_in);
			socket.close();
			exit(0);
		}

		System.out.println(message_in);

		ClientThread cl = new ClientThread(socket, this);
		cl.start();

		System.out.println("To close the connection, enter END\n"
				+ "To Message a client, enter Message\n"
				+ "To Refresh Messages, enter Refresh");
		message_out = input.nextLine();

		while (!message_out.equals("END")) {

			switch (message_out) {
			case "Message":
				System.out.print("Enter the destination email: ");
				String cname = input.nextLine().trim();

				System.out.print("Enter the subject: ");
				String subject = input.nextLine().trim();

				System.out.println("Enter the content of your message to send: ");
				String content = input.nextLine().trim();

				message_out += ":" + cname + ":" + subject + ":" + content;
				out.writeUTF(message_out);
				objectOut.writeObject(new Email(cname, subject, content));
				
				// TODO wait for puzzle..
				break;
			case "Refresh":
				objectOut.writeUTF("Refresh");
//				ArrayList<Email> emails = (ArrayList<Email>) objectIn.readObject();
				break;
			default:
				System.out.println("The command is incorrect please enter again.");
				break;
			}

			System.out.println("To close the connection, enter END\n" 
			+ "To Send a message, enter Message\n"
			+ "To Refresh Messages, enter Refresh");
			message_out = input.nextLine();
		}

		input.close();
		System.out.println("Connection is closing...");
		socket.close();
		System.out.println("Connection succesfully closed...");

	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public DataInputStream getIn() {
		return in;
	}

	public void setIn(DataInputStream in) {
		this.in = in;
	}

	public DataOutputStream getOut() {
		return out;
	}

	public void setOut(DataOutputStream out) {
		this.out = out;
	}

	public ObjectInputStream getObjectIn() {
		return objectIn;
	}

	public void setObjectIn(ObjectInputStream objectIn) {
		this.objectIn = objectIn;
	}

	public ObjectOutputStream getObjectOut() {
		return objectOut;
	}

	public void setObjectOut(ObjectOutputStream objectOut) {
		this.objectOut = objectOut;
	}

	public String getMessage_in() {
		return message_in;
	}

	public void setMessage_in(String message_in) {
		this.message_in = message_in;
	}

	public String getMessage_out() {
		return message_out;
	}

	public void setMessage_out(String message_out) {
		this.message_out = message_out;
	}

	public ArrayList<Email> getEmails() {
		return emails;
	}

	public void setEmails(ArrayList<Email> emails) {
		this.emails = emails;
	}
}
