package tcsmp.puzzle;

import java.util.Arrays;

import tcsmp.utils.DataUtils;

public class Block {

	private String[] directions;
	
	public Block() {
		directions = new String[4];
		this.fillDirections();
	}
	
	public Block(String directions) {
		if(directions != null && directions.length() == 4) {
			this.directions = directions.substring(0,4).split("");
		}
	}
	
	private void fillDirections() {
		if(directions != null) {
			for(int i=0; i < directions.length; i++) {
				if(directions[i] == null || directions[i].isEmpty()) {
					directions[i] = DataUtils.generateRandomChar();
				}
			}
		}
	}

	public String[] getDirections() {
		return directions;
	}

	public void setDirections(String[] directions) {
		this.directions = directions;
	}
	
	@Override
	public String toString() {
		String s = "";
		if(directions != null) {
			for(int i=0; i < directions.length; i++)
				s += directions[i];
		}
		return s;
	}
}
