package tcsmp.client;

import static java.lang.System.exit;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import tcsmp.utils.HostPort;

public class Client {
	private Socket socket;
	private Integer port;
	private String host;
	private String email;

	private DataInputStream in;
	private DataOutputStream out;

	private String message_in;
	private String message_out;

	public Client(String email, HostPort hp) throws IOException {
		this.email = email;
		this.port = hp.getPort();
		this.host = hp.getHost();

		try {
			socket = new Socket(host, port);
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			
		} catch (IOException ex) {
			System.out.println("Host not reachable!");
			exit(0);
		}
	}

	public void start() throws IOException {
		Scanner input = new Scanner(System.in);

//		System.out.println("Please Enter your name:");
//		String name = input.nextLine();
//		String req = "Register:" + name;
		String req = "Register:" + email;

		out.writeUTF(req);
		message_in = in.readUTF();
		if (!message_in.startsWith("REGISTRATION OK")) {
			System.out.println(message_in);
			socket.close();
			exit(0);
		}

		System.out.println(message_in);

		ClientThread cl = new ClientThread(socket);
		cl.start();

		System.out.println("To close the connection, enter END\nTo Message a client, enter Message");
		message_out = input.nextLine();

		while (!message_out.equals("END")) {

			switch (message_out) {
			case "Message":
				System.out.print("Enter the destination email: ");
				String cname = input.nextLine();

				System.out.print("Enter the subject: ");
				String subject = input.nextLine();

				System.out.println("Enter the content of your message to send: ");
				String content = input.nextLine();

				message_out += ":" + cname + ":" + subject + ":" + content;
				out.writeUTF(message_out);

				// TODO wait for puzzle..
				break;
			case "Refresh":
				break;
			default:
				System.out.println("The command is incorrect please enter again.");
				break;
			}

			System.out.println("To close the connection, enter END\n" + "To Send a message, enter Message");
			message_out = input.nextLine();
		}

		input.close();
		System.out.println("Connection is closing...");
		socket.close();
		System.out.println("Connection succesfully closed...");

	}
}
