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

import tcsmp.application.ClientMain;
import tcsmp.controller.ComposeController;
import tcsmp.controller.PuzzleController;
import tcsmp.puzzle.Puzzle;
import tcsmp.utils.Email;

public class ClientThread extends Thread {

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
				String[] tokens = message_in.split(":");
				if (message_in.equals("Refresh")) {
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
					ClientMain.current_user.setEmails(emails);
				} else if(tokens[0].equals("puzzle")){
					Puzzle puzzle = new Puzzle(tokens[1].toCharArray());
			    	PuzzleController.p = puzzle;
				} else if(tokens[0].equals("Success")) {
					PuzzleController.correctPuzzel = true;
					System.out.println(tokens[1]);
				}
				else if(tokens[0].equals("Failed")) {
					PuzzleController.correctPuzzel = false;
					System.out.println(tokens[1]);
				} else if(tokens[0].equals("NotFound")) {
					ComposeController.clientFound = false;
				}
				else {
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
