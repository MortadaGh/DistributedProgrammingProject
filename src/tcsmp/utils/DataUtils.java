package tcsmp.utils;

import java.util.Random;

public class DataUtils {

	public static String generateRandomChar() {
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
//		String fullAlphabet = alphabet + alphabet.toLowerCase();
		Random r = new Random();
		return alphabet.charAt(r.nextInt(alphabet.length())) + "";
	}
}
