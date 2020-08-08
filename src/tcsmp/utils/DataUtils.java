package tcsmp.utils;

import java.util.HashMap;
import java.util.Random;

import tcsmp.server.Server;

public class DataUtils {

	public static final String DOMAIN_NAME_PATTERN = "^((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\.)+[A-Za-z]{2,6}$";
	public static HashMap<String, Integer> servers = new HashMap<String, Integer>();
	
	public static String generateRandomChar() {
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
//		String fullAlphabet = alphabet + alphabet.toLowerCase();
		Random r = new Random();
		return alphabet.charAt(r.nextInt(alphabet.length())) + "";
	}
}
