package tcsmp.server;

import java.util.Scanner;

import tcsmp.utils.DataUtils;

public class ServerMain {

	@SuppressWarnings({ "resource", "unlikely-arg-type" })
	public static void main(String[] args) {
		while (true) {
			Scanner in = new Scanner(System.in);

			System.out.print("Enter domain name: ");
			String domainName = in.nextLine();

			if (!domainName.matches(DataUtils.DOMAIN_NAME_PATTERN)) {
				System.out.println("Invalid domain name");
				System.exit(0);
			}

			System.out.print("Enter port number: ");
			String portNumber = in.nextLine();
			Integer port = -1;
			try {
				port = Integer.parseInt(portNumber);
				if (port < 1000)
					throw new Exception();

				if (DataUtils.servers.containsKey(port)) {
					System.out.println("port number is taken!");
				}
			} catch (Exception e) {
				System.out.println("Invalid port number!");
				System.exit(0);
			}

			Server server = new Server(domainName, port);
			DataUtils.servers.put(domainName, port);
			server.start();
			
			System.out.println("Running Servers: ");
			DataUtils.servers.forEach((d, p) -> {
				System.out.println(d + " : " + p);
			});
			System.out.println();
		}
	}
}
