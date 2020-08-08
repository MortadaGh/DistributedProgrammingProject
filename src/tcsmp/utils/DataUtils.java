package tcsmp.utils;

import java.util.HashMap;
import java.util.Random;

public class DataUtils {

	public static final String DOMAIN_NAME_PATTERN = "^((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\.)+[A-Za-z]{2,6}$";
	public static HashMap<String, HostPort> servers = new HashMap<String, HostPort>();
	private static final String zeroTo255 = "(\\d{1,2}|(0|1)\\" + "d{2}|2[0-4]\\d|25[0-5])";
	public static final String IP_PATTERN = zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255;
	public static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

	static {
		servers.put("kardon.com", new HostPort("localhost", 1234));
		servers.put("mg.com", new HostPort("localhost", 1235));
	}
	
	public static String generateRandomChar() {
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
//		String fullAlphabet = alphabet + alphabet.toLowerCase();
		Random r = new Random();
		return alphabet.charAt(r.nextInt(alphabet.length())) + "";
	}
}
