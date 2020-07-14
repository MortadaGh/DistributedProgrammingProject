package tcsmp.puzzle;

import tcsmp.utils.DataUtils;

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
		return s == null ? false : (s.length() != 16 ? false : (!s.matches("[A-Z]*") ? false : s.equals(this.toString())));
	}

	@Override
	public String toString() {
		String s = "";
		if(blocks != null) {
			for (int i = 0; i < blocks.length; i++)
				s += blocks[i];			
		}
		return s;
	}

	public static Puzzle parse(String s) {
		if(s != null && s.length() == 16) {
			Block[] blocks = new Block[4];
			for(int i = 0; i < blocks.length; i++) {
				blocks[i] = new Block(s.substring(i*4,(i*4) + 4));
			}
			return new Puzzle(blocks);
		}
		return null;
	}

	public static Puzzle generatePuzzle() {
		// generate 4 random characters
		String[] r = new String[4];
		for(int i=0; i < r.length; i++) {
			r[i] = DataUtils.generateRandomChar();
		}
		
		// create 4 blocks
		Block[] blocks = new Block[4];
		for(int i = 0; i < blocks.length; i++) {
			blocks[i] = new Block();
		}
		
		// Rule 1
		blocks[0].getDirections()[1] = r[0];
		blocks[1].getDirections()[3] = r[0];

		// Rule 2
		blocks[0].getDirections()[2] = r[1];
		blocks[2].getDirections()[0] = r[1];
		
		// Rule 3
		blocks[1].getDirections()[2] = r[2];
		blocks[3].getDirections()[0] = r[2];
		
		// Rule 4
		blocks[2].getDirections()[1] = r[3];
		blocks[3].getDirections()[3] = r[3];
		
		return new Puzzle(blocks);
	}

}
