package tcsmp.puzzle;

import java.util.Arrays;

public class Block {

	private char[] directions;
	
	public Block(String directions) {
		this.directions = directions.toCharArray();
	}
	
	public void fillDirections() {
		
	}

	public char[] getDirections() {
		return directions;
	}

	public void setDirections(char[] directions) {
		this.directions = directions;
	}

	public Block parse(String s) {
		return null;
	}
	
	@Override
	public String toString() {
		return Arrays.toString(directions);
	}
}
