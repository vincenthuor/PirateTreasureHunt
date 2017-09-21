package redbeard;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import heap.HeapEmptyException;
import heap.HeapFullException;
import world.Grid;
import world.Node;

public class TreasureHunt {

	private final int DEFAULT_SONARS = 3; // default number of available sonars
	private final int DEFAULT_RANGE = 200; // default range of a sonar
	protected Grid islands; // the world where the action happens!
	protected int height, width, landPercent;
	protected int sonars, range; // user defined number of sonars and range
	protected String state; // state of the game (STARTED, OVER)
	protected ArrayList<Node> path; // the path to the treasure!

	public TreasureHunt() {
		// The default constructor
		this.state = "STARTED";
		this.sonars = DEFAULT_SONARS;
		this.range = DEFAULT_RANGE;
		this.islands = new Grid(); // set with default grid values
		this.height = this.islands.getHeight();
		this.width = this.islands.getWidth();
		this.landPercent = this.islands.getPercent();
	}

	public TreasureHunt(int height, int width, int landPercent, int sonars,
			int range) {
		// The constructor that uses parameters
		this.state = "STARTED";
		this.sonars = sonars;
		this.range = range;
		this.islands = new Grid(width, height, landPercent); // set with custom values
		this.height = this.islands.getHeight();
		this.width = this.islands.getWidth();
		this.landPercent = this.islands.getPercent();
	}

	private void processCommand(String command) throws HeapFullException,
			HeapEmptyException {
		// The allowed commands are: 
		// SONAR to drop the sonar in hope to detect treasure
		// GO direction to move the boat in some direction
		// For example, GO NW means move the boat one cell up left
		// (if the cell is navigable; if not simply ignore the command)
		
		if (command.length() > 0) {
			String[] lineData2 = command.split("\\s+");
		
			if (lineData2[0].equals("SONAR")) {
				Node treasureNode = islands.getTreasure(this.range);
				if (treasureNode != null) {
					Node boatNode = this.islands.getBoat();
					
					// A* algorithm
					this.islands.findPath(boatNode, treasureNode);
					// retrace path
					this.path = this.islands.retracePath(boatNode, treasureNode);
					// CASE: treasure.parent is null -> unreachable
					// RetracePath(Node, Node) will always return treasure node regardless
					// Unable to access protected variable Node.parent to check for null
					// this.pathlength will be 1 and the node will have a null parent if treasure
					// is unreachable
//					if (this.path.size() == 1 && this.path.get(0).parent == null) {
//						this.path = null;
//					}

					// end the turn
					return;
				}
			}
			else if (lineData2[0].equals("GO")) {
				if (lineData2[1].equals("N")) {
					this.islands.move("N");
				}
				else if (lineData2[1].equals("S")) {
					this.islands.move("S");
				}
				else if (lineData2[1].equals("E")) {
					this.islands.move("E");
				}
				else if (lineData2[1].equals("W")) {
					this.islands.move("W");
				}
				else if (lineData2[1].equals("NE")) {
					this.islands.move("NE");
				}
				else if (lineData2[1].equals("NW")) {
					this.islands.move("NW");
				}
				else if (lineData2[1].equals("SE")) {
					this.islands.move("SE");
				}
				else if (lineData2[1].equals("SW")) {
					this.islands.move("SW");
				}
			}
		}
	}

	public int pathLength() {
		if (path == null)
			return 0;
		else return path.size();
	}

	public String getMap() {
		return islands.drawMap();
	}

	public void play(String pathName) throws FileNotFoundException,
			HeapFullException, HeapEmptyException {
		// Read a batch of commands from a text file and process them.
		
		Scanner getData = new Scanner(new File(pathName));
		if (!getData.hasNextLine()) {
			getData.close();
		}
		
//		System.out.println(getMap()); // debug
		while (getData.hasNextLine()) {
		
			String line2 = getData.nextLine(); // debug
			if (line2.startsWith("SONAR") || line2.startsWith("GO")) { // security check
				processCommand(line2);
//		        System.out.println(getMap()); // debug
			}
			if (this.path != null) {
				break;
			}
			
		}
		getData.close();
		this.state ="OVER";
	}

}
