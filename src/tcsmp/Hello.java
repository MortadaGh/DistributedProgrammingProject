package tcsmp;

import tcsmp.puzzle.Block;
import tcsmp.puzzle.Puzzle;

public class Hello {

	public static void main(String[] args) {
		System.out.println("Hello World!");
		
//		Block b1 = new Block("ABCD");
//		Block b2 = new Block("EFGH");
//		Block b3 = new Block("IJKL");
//		Block b4 = new Block("MNOP");
//
//		Block[] blocks = new Block[] { b1, b2, b3, b4 };
//		Puzzle p = new Puzzle(blocks);
//		System.out.println("p = " + p);
//		
//		String r = "ABCDEFGHIJKLMNOP";
//		Puzzle p2 = Puzzle.parse(r);
//		System.out.println(p2);
		
		System.out.println(Puzzle.generatePuzzle());
	}

}
