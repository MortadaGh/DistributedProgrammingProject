package tcsmp.puzzle;

import java.util.Arrays;

public class Puzzle {

	private Block[] blocks;

	public Puzzle(Block[] blocks) {
		this.blocks = blocks;
	}

	public Block[] getBlocks() {
		return blocks;
	}

	public void setBlocks(Block[] blocks) {
		this.blocks = blocks;
	}

	public Boolean matches(String s) {
		return true;
	}
	
	public Puzzle parse(String s) {
		return null;
	}
	
	@Override
	public String toString() {
		return Arrays.toString(blocks);
	}
	
	public static Puzzle generatePuzzle() {
		return null;
	}
	
	
}
