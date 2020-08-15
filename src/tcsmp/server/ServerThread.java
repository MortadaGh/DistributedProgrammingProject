package tcsmp.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import tcsmp.exceptions.RegistrationException;
import tcsmp.puzzle.Puzzle;
import tcsmp.utils.DataUtils;
import tcsmp.utils.Email;
import tcsmp.utils.HostPort;

public class ServerThread extends Thread {

	private String email;
	private Server server;

	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
//	private ObjectInputStream objectIn;
//	private ObjectOutputStream objectOut;

	public ServerThread(Socket link, Server server) {
		this.socket = link;
		this.server = server;

		try {
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
//			objectOut = new ObjectOutputStream(socket.getOutputStream());
//			objectIn = new ObjectInputStream(socket.getInputStream());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void run() {
		String message_in;
		Boolean isServer = false;

		try {
			message_in = in.readUTF();
//			String[] tokens = message_in.split(":", 2);
			String[] tokens = message_in.split(":");

			if (tokens[0].equals("Server-Domain")) {
				isServer = true;
				String[] emailArray = tokens[3].split(";", 4);
				String email = emailArray[1];
				String subject = emailArray[2];
				String content = emailArray[3];
				
				boolean found = server.getEmails().containsKey(email);
				out.writeBoolean(found);
								
				if(found) {
					Email emailObj = new Email(email, tokens[2], subject, content);
					
					//generate puzzle
					Puzzle puzzle = new Puzzle();
					puzzle.generate();
					System.out.println("NOT MIXED: "+ puzzle.toString());
					puzzle.mix();
					System.out.println("MIXED: "+ puzzle.toString());
					out.writeUTF("puzzle:"+puzzle.toString());
					String answer = in.readUTF();
					System.out.println("Answer:" + answer);
					boolean correct = puzzle.verify(answer);
					System.out.println(correct);
					if(correct) {
						server.getEmails().get(email).add(emailObj);
						out.writeUTF("Success:Email sent succesfully!");
					} else {
						out.writeUTF("Failed:Email not sent!");
					}					
				}
			} else {
				if (tokens[0].equals("Register")) {
					email = tokens[1];
					System.out.println("REGISTRATION OK - " + email);
					out.writeUTF("REGISTRATION OK - " + email);
					server.getEmails().put(email, new ArrayList<Email>());
				} else {
					System.out.println("Registration not established!");
					out.writeUTF("Registration not established!");
					throw new RegistrationException();
				}

				do {
						message_in = in.readUTF();
						tokens = message_in.split(":", 4);


					if (message_in.equals("END")) {
						System.out.println("Bye bye!");
						break;
					} else if (tokens[0].equals("Message")) {
						String dest = tokens[1];
						String subject = tokens[2];
						String content = tokens[3];
						Email emailObj = new Email(dest, email, subject, content);
						System.out.println("Email = " + emailObj);

						boolean found = false;
						String[] destinationEmail = dest.split("@", 2);
						String[] hostEmail = email.split("@", 2);

						if (!destinationEmail[1].equals(hostEmail[1])) {
							String responseFromServer;
							try {
								HostPort destinationHP = DataUtils.servers.get(destinationEmail[1]);
								HostPort hostPort = DataUtils.servers.get(hostEmail[1]);

								// Create new Socket with the destination host/port and then send the email to
								// the destination server
								Socket socketServer = new Socket(destinationHP.getHost(), destinationHP.getPort());
								DataOutputStream serverOut = new DataOutputStream(socketServer.getOutputStream());
								System.out.println("Sending Email to " + dest + " AT port " + destinationHP.getPort());
								String message_out = "Server-Domain:" + dest + ":" + email + ":" + String.join(";", tokens);
								serverOut.writeUTF(message_out);
								//out.writeUTF("Trying to communicate with " + dest);

								DataInputStream serverIn = new DataInputStream(socketServer.getInputStream());
								
								boolean foundOnSecondServer = serverIn.readBoolean();
								if(foundOnSecondServer) {						
									found = true;
									
									//ask for a puzzle from server2
									String puzzleString = serverIn.readUTF();
									
									//send puzzle to the client to solve it
									out.writeUTF(puzzleString);
									
									//wait client answer
									String answer = in.readUTF();
									
									//send answer to the server2
									serverOut.writeUTF(answer);
									
									//wait for server2 response
									responseFromServer = serverIn.readUTF();
									
									//notify the client
									out.writeUTF(responseFromServer);
								}
							} catch (IOException ex) {
								ex.printStackTrace();
							}
						} else {
							found = server.getEmails().containsKey(dest);
							if (found) {
								//generate puzzle
								Puzzle puzzle = new Puzzle();
								puzzle.generate();
								puzzle.mix();
								//send it to client
								//wait for client solutions
								boolean correct = false;
								String answer = "";
//								do{
								out.writeUTF("puzzle:"+puzzle.toString());
								System.out.println("Puzzle sent to client : " + puzzle);
								answer = in.readUTF();
								correct = puzzle.verify(answer);
//								} while (!correct || answer.equals("cancel"));
								
								if(correct) {
									server.getEmails().get(dest).add(emailObj);
									System.out.println("server.getEmails() = " + server.getEmails());
									out.writeUTF("Success:Email sent succesfully!");
								} else {
									out.writeUTF("Failed:Email not sent!");
								}	

							}
						}
						if (!found) {
							out.writeUTF("NotFound:Client not found");
						}
					} else if (tokens[0].equals("Refresh")) {
						out.writeUTF("Refresh");
						ArrayList<Email> emails = (ArrayList<Email>) server.getEmails().get(email);
						System.out.println("emails = " + emails);
						out.writeInt(emails.size());
						for (Email email : emails) {
							out.writeUTF(email.getTo());
							out.writeUTF(email.getFrom());
							out.writeUTF(email.getSubject());
							out.writeUTF(email.getContent());
						}
					}
				} while (true);
				
			}
			closeconnection();

		} catch (EOFException ex1) {
			closeconnection();
//		} catch (SocketException e) {
//			closeconnection();
		} catch (RegistrationException e) {
			closeconnection();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void closeconnection() {
		server.getClientsSockets().remove(this.socket);
		try {
			socket.close();
		} catch (IOException ex) {
			Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
