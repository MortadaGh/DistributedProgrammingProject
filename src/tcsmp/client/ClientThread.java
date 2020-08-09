package tcsmp.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import tcsmp.puzzle.Puzzle;
import tcsmp.utils.Email;

class ClientThread extends Thread {

	private Client client;
	private Socket link;
	private DataInputStream in;
	private DataOutputStream out;
//	private ObjectInputStream objectIn;
//	private ObjectOutputStream objectOut;

	public ClientThread(Socket link, Client client) {
		this.link = link;
		this.client = client;
//		try {
//			in = new DataInputStream(link.getInputStream());
//			out = new DataOutputStream(link.getOutputStream());
		in = client.getIn();
		out = client.getOut();
//			objectOut = new ObjectOutputStream(link.getOutputStream());
//			objectIn = new ObjectInputStream(link.getInputStream());
//			objectIn = client.getObjectIn();
//		} catch (IOException ex) {
//			ex.printStackTrace();
//		}
	}

	public void run() {
		Scanner input = new Scanner(System.in);
		while (true) {
			try {
				String message_in = in.readUTF();
				if (message_in.equals("Refresh")) {
					// TODO
//					ArrayList<Email> emails = (ArrayList<Email>) in.readObject();
					ArrayList<Email> emails = new ArrayList<Email>();
					int size = in.readInt();
					for (int i = 0; i < size; i++) {
						String to = in.readUTF().trim();
						String from = in.readUTF().trim();
						String subject = in.readUTF().trim();
						String content = in.readUTF().trim();
						Email email = new Email(to, from, subject, content);
						emails.add(email);
					}
					System.out.println("emails = " + emails);
					client.setEmails(emails);
				} else if(message_in.toLowerCase().startsWith("puzzle")){
					String puzzleString = message_in.split(":")[2].toUpperCase().trim();
//					System.out.println("message_in.split(\":\") = " + Arrays.toString(message_in.split(":")));
//					System.out.println("puzzleString = " + puzzleString);
					Puzzle puzzle = Puzzle.parse(puzzleString);
//					System.out.println("puzzle = " + puzzle);
					System.out.println("Solve this Puzzle: " + puzzle.toString());
					System.out.print("Answer: ");
//					String answer = input.nextLine().toUpperCase().trim();
//					out.writeUTF(answer);
				} else {
					System.out.println(message_in);
				}
			} catch (SocketException ex) {
				break;
			} catch (IOException ex) {
				ex.printStackTrace();
				break;
			}
		}
	}
}
