package tcsmp.client;

import java.io.IOException;
import java.util.Scanner;

import tcsmp.utils.DataUtils;
import tcsmp.utils.HostPort;

public class ClientMain {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);

		//System.out.println("TEST : " + DataUtils.servers);

		// Register
		System.out.print("Enter your email: ");
		String email = in.nextLine();

		if (!email.matches(DataUtils.EMAIL_PATTERN)) {
			System.out.println("Invalid email");
			System.exit(0);
		}

		String[] splittedEmail = email.split("@");
//		String name = splittedEmail[0];
		String domainName = splittedEmail[1];

		if (!DataUtils.servers.containsKey(domainName)) {
			System.out.println("Invalid domain name");
			System.exit(0);
		}

		HostPort hp = DataUtils.servers.get(domainName);

//		System.out.print("Enter your host: ");
//		String host = in.nextLine();
//
//		if (!(host.matches(DataUtils.IP_PATTERN) || host.equals("localhost"))) {
//			System.out.println("Invalid host");
//			System.exit(0);
//		}
//		
//		System.out.print("Enter port number: ");
//		String portNumber = in.nextLine();
//		Integer port = -1;
//		try {
//			port = Integer.parseInt(portNumber);
//			if (port < 1000)
//				throw new Exception();
//
//			if (!DataUtils.servers.containsValue(port)) {
//				System.out.println("no server running on this port!");
//				throw new Exception();
//			}
//		} catch (Exception e) {
//			System.out.println("Invalid port number!");
//			System.exit(0);
//		}

		try {
			Client client = new Client(email, hp);
			client.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		in.close();
	}

}
