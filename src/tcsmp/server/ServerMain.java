package tcsmp.server;

import java.net.UnknownHostException;
import java.util.Map.Entry;

import tcsmp.utils.DataUtils;
import tcsmp.utils.HostPort;

public class ServerMain {

	public static void main(String[] args) throws UnknownHostException {

		for (Entry<String, HostPort> o : DataUtils.servers.entrySet()) {
			Server server = new Server(o.getKey(), o.getValue().getPort());
			server.start();
		}

		/*
		 * while (true) { Scanner in = new Scanner(System.in);
		 * 
		 * System.out.print("Enter domain name: "); String domainName = in.nextLine();
		 * 
		 * if (!domainName.matches(DataUtils.DOMAIN_NAME_PATTERN)) {
		 * System.out.println("Invalid domain name"); System.exit(0); }
		 * 
		 * System.out.print("Enter port number: "); String portNumber = in.nextLine();
		 * Integer port = -1; try { port = Integer.parseInt(portNumber); if (port <
		 * 1000) throw new Exception();
		 * 
		 * if (DataUtils.servers.containsValue(port)) {
		 * System.out.println("port number is taken!"); throw new Exception(); } } catch
		 * (Exception e) { System.out.println("Invalid port number!"); System.exit(0); }
		 * 
		 * String host = InetAddress.getLocalHost().getHostAddress();
		 * 
		 * HostPort hp = new HostPort(host, port); Server server = new
		 * Server(domainName, port); DataUtils.servers.put(domainName, hp);
		 * server.start();
		 * 
		 * System.out.println("====================");
		 * System.out.println("Running Servers:");
		 * System.out.println("----------------"); DataUtils.servers.forEach((d, p) ->
		 * System.out.println(d + " : " + p));
		 * System.out.println("===================="); }
		 */
	}
}
