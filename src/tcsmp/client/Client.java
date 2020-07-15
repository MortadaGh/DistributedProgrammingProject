package tcsmp.client;

import static java.lang.System.exit;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static Socket socket;
    public static int PORT = 1234;
    public static String host = "localhost";
    
    public static void main(String[] args) {
        String message_in;
        String message_out;
        try {
            socket = new Socket(host,PORT);
        } catch (IOException ex) {
            System.out.println("Host not reachable!");
            exit(0);
        }
        
        try {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            
            Scanner input = new Scanner(System.in);
            
            System.out.println("Please Enter your name:");
            String name = input.nextLine();
            String req = "Register:" + name;
            
            out.writeUTF(req);
            message_in = in.readUTF();
            if(!message_in.equals("REGISTRATION OK")){
                System.out.println(message_in);
                socket.close();
                exit(0);
            }
            
            ClientThread cl = new ClientThread(socket);
            cl.start();
            
            System.out.println("To close the connection, enter END\nTo Broadcast a message, enter Broadcast\nTo Message a client, enter Message");
            message_out = input.nextLine();
            
            while(!message_out.equals("END")){
                
                if(message_out.equals("Broadcast")) {
                	System.out.println("Enter the content of your message to send:");
                	message_out = message_out + ":" + input.nextLine();
                	out.writeUTF(message_out);
                } else if(message_out.equals("Message")) {
                	System.out.println("Enter the name of your client: ");
                	String cname = input.nextLine();
                	System.out.println("Enter the content of your message to send: ");
                	message_out = message_out + ":" + cname + ":" + input.nextLine();
                	out.writeUTF(message_out);
                } else {
                	System.out.println("The command is incorrect please enter again.");                	
                }
                
                System.out.println("To close the connection, enter END\n"
		                		+ "To Broadcast a message, enter Broadcast\n"
		                		+ "To Send a message, enter Message");
                message_out = input.nextLine();
            }
            
            input.close();
            System.out.println("Connection is closing...");
            socket.close();
            System.out.println("Connection succesfully closed...");
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
  
    }
}
