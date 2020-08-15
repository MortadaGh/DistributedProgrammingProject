package tcsmp.client;

import static java.lang.System.exit;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tcsmp.application.ClientMain;
import tcsmp.controller.AlertController;
import tcsmp.puzzle.Puzzle;
import tcsmp.utils.Email;
import tcsmp.utils.HostPort;

public class Client implements Serializable {

	private static final long serialVersionUID = 5734917173628924548L;

	private Socket socket;
	private Integer port;
	private String host;
	private String email;

	private DataInputStream in;
	private DataOutputStream out;
//	private ObjectInputStream objectIn;
//	private ObjectOutputStream objectOut;

	private String message_in;
	private String message_out;

	private ArrayList<Email> emails = new ArrayList<Email>();

	public Client(String email, HostPort hp) {
		this.email = email;
		this.port = hp.getPort();
		this.host = hp.getHost();

		try {
			socket = new Socket(host, port);
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException ex) {
			AlertController.error("Server is not reachable!");
		}
	}
	
	public ObservableList<Email> getEmailsForUI(){
		ObservableList<Email> messages = FXCollections.observableArrayList();
		String[] domainName = ClientMain.current_user.getEmail().split("@");
		String body = "Welcome "+ ClientMain.current_user.getEmail() + " to the TCSMP project\nThis Project is made by: \nMortada Ghanem 91907\nHassan Assaad 86296\n\nRegards";
		messages.add(new Email(ClientMain.current_user.getEmail(),"Server@"+domainName[1],"Welcome Email", body));
		
		for(int i=0; i < this.emails.size(); i++) {
			Email email = this.emails.get(i);
			messages.add(email);
		}
		return messages;
	}

//	public void start() {
//		Scanner input = new Scanner(System.in);
//		try {
//
//			String req = "Register:" + email;
//
//			out.writeUTF(new String(req));
//
//			message_in = in.readUTF();
//			if (!message_in.startsWith("REGISTRATION OK")) {
//				System.out.println(message_in);
//				socket.close();
//				exit(0);
//			}
//
//			System.out.println(message_in);
//
//			ClientThread cl = new ClientThread(socket, this);
//			cl.start();
//
//			System.out.println("To close the connection, enter END\n" + "To Message a client, enter Message\n"
//					+ "To Refresh Messages, enter Refresh");
//			message_out = input.nextLine();
//
//			while (!message_out.equals("END")) {
//
//				switch (message_out) {
//				case "Message":
//					System.out.print("Enter the destination email: ");
//					String cname = input.nextLine().trim();
//
//					System.out.print("Enter the subject: ");
//					String subject = input.nextLine().trim();
//
//					System.out.println("Enter the content of your message to send: ");
//					String content = input.nextLine().trim();
//
//					message_out += ":" + cname + ":" + subject + ":" + content;
//					out.writeUTF(message_out);
////					out.writeObject(new Email(cname, subject, content));
//
//					// wait for puzzle..
//					String answer = input.nextLine().toUpperCase().trim();
//					out.writeUTF(answer);
////					Puzzle puzzle = (Puzzle) in.readObject();
////					System.out.println("Solve this puzzle: " + puzzle.toString());
////					System.out.print("Answer: ");
////					String answer = input.nextLine().toUpperCase().trim();
//
////					out.writeUTF(answer);
//					break;
//				case "Refresh":
//					out.writeUTF("Refresh");
////					ArrayList<Email> emails = (ArrayList<Email>) objectIn.readObject();
//					break;
//				default:
//					System.out.println("The command is incorrect please enter again.");
//					break;
//				}
//
//				System.out.println("To close the connection, enter END\n" + "To Send a message, enter Message\n"
//						+ "To Refresh Messages, enter Refresh");
//				message_out = input.nextLine();
//
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		try {
//			input.close();
//			System.out.println("Connection is closing...");
//			socket.close();
//			System.out.println("Connection succesfully closed...");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

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
	
	public Client getClient() {
		return this;
	}
}
