package redbeard;

import heap.HeapEmptyException;
import heap.HeapFullException;

import java.io.FileNotFoundException;

public class GameTest {

	/**
	 * @param args
	 * @throws HeapEmptyException 
	 * @throws HeapFullException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, HeapFullException, HeapEmptyException {
		String pathname = args[0];
		TreasureHunt game = new TreasureHunt();

		game.play(pathname);
        System.out.println(game.getMap());
        System.out.println(game.state);
        System.out.println(game.pathLength());
	}

}
