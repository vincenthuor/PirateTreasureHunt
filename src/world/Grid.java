package world;

import heap.Heap;
import heap.HeapEmptyException;
import heap.HeapFullException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Grid {

	private final int DEFAULT_WIDTH = 60; // default width of the world map - gridX runs from 0 to 59
	private final int DEFAULT_HEIGHT = 15; // default height of the map - gridY runs from 0 to 14
	private final int DEFAULT_PERCENT = 20; // this is the percentage of the map occupied by islands
	protected int width, height; // user defined width and height, if one is not using defaults
	protected int percent; // user defined percentage of islands on the map
	protected Node treasure; // points to the map node where the Redbeard treasure is sunken
	protected Node boat; // points to the current location of our boat on the map

	protected Node[][] map; // the map

	public Grid() {
		width = DEFAULT_WIDTH;
		height = DEFAULT_HEIGHT;
		percent = DEFAULT_PERCENT;
		buildMap();
	}

	public Grid(int width, int height, int percent) {
		this.width = width;
		this.height = height;
		if (percent <= 0 || percent >= 100)
			this.percent = DEFAULT_PERCENT;
		else
			this.percent = percent;
		buildMap();
	}

	private void buildMap() {
		// For each map position (i,j) you need to generate a
		// Node that can be navigable or it may belong to an island
		// Generate the location of the boat and of the treasure; 
		// they must be on navigable waters, not on the land!
		Random rand = new Random();
		map = new Node[this.height][this.width]; // an Array of Array of Nodes
		int maxPercentage = 100;
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int num = rand.nextInt(maxPercentage);
				if (num <= getPercent()) {
					map[i][j] = new Node(false, j, i);
				}
				else {
					map[i][j] = new Node(true, j, i);
				}
			}
		}
		boatTreasureGenerator();
		
	}
	private void boatTreasureGenerator() {
		// Generates boat or treasure depending of what is passed in.
		// Called by buildMap();
		Random rand = new Random();
		
		ArrayList<Node> emptySpots = new ArrayList<Node>();
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (map[i][j].walkable) {
					emptySpots.add(map[i][j]);
				}
			}
		}
		if (emptySpots.size() > 2) { // must leave enough space to generate boat and treasure;
			
			int availableNum = rand.nextInt(emptySpots.size());
			int availableNum2 = rand.nextInt(emptySpots.size());
		
			this.boat = emptySpots.get(availableNum);
			this.treasure = emptySpots.get(availableNum2);
		}
	}

	public String drawMap() {
		String result = "";
		String hline = "       ";
		String extraSpace;
		for (int i = 0; i < width / 10; i++)
			hline += "         " + (i + 1);
		result += hline + "\n";
		hline = "       ";
		for (int i = 0; i < width; i++)
			hline += (i % 10);
		result += hline + "\n";
		for (int i = 0; i < height; i++) {
			if (i < 10)
				extraSpace = "      ";
			else
				extraSpace = "     ";
			hline = extraSpace + i;
			for (int j = 0; j < width; j++) {
				if (i == boat.gridY && j == boat.gridX)
					hline += "B";
				else if (i == treasure.gridY && j == treasure.gridX)
					hline += "T";
				else if (map[i][j].inPath)
					hline += "*";
				else if (map[i][j].walkable)
					hline += ".";
				else
					hline += "+";
			}
			result += hline + i + "\n";
		}
		hline = "       ";
		for (int i = 0; i < width; i++)
			hline += (i % 10);
		result += hline + "\n";
		return result;
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getPercent() {
		return percent;
	}
	
	public Node getBoat() {
		return boat;
	}
	
	private ArrayList<Node> getNeighbours(Node node) {
		// each node has at most 8 neighbors
		ArrayList<Node> result = new ArrayList<Node>();
		
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				if (x == 0 && y == 0) {
					continue;
				}
				int yCheck = node.gridY + y;
				int xCheck = node.gridX + x;
				if (yCheck >= 0 && yCheck < this.height && xCheck >= 0 && xCheck < this.width) {
					result.add(this.map[yCheck][xCheck]);
				}
			}
		}
		return result;
	}
		
	private int getDistance(Node nodeA, Node nodeB) {
		int dstX = Math.abs(nodeA.gridX - nodeB.gridX);
		int dstY = Math.abs(nodeA.gridY - nodeB.gridY);
		if (dstX > dstY)
			return 14 * dstY + 10 * (dstX - dstY);
		return 14 * dstX + 10 * (dstY - dstX);
	}

	public void findPath(Node startNode, Node targetNode)
			throws HeapFullException, HeapEmptyException {
		// This method implements A-star path search algorithm.	
		if (startNode == null || targetNode == null) {
			return;
		}
		Heap<Node> openSet = new Heap<Node>(width * height); // this where we make use of our heaps
		HashSet<Node> closedSet = new HashSet<Node>(width * height);
		openSet.add(startNode); // add first node to Heap
		
		while (openSet.count() > 0) {
			Node parentNode = openSet.removeFirst();
			closedSet.add(parentNode);
			
			if (parentNode == targetNode) {
//				retracePath(startNode, targetNode); 
				return;
			}
			
			ArrayList<Node> neighbourS = getNeighbours(parentNode);
			
			for (Node neighbour: neighbourS) {
				if (!neighbour.walkable || closedSet.contains(neighbour)) {
					continue;
				}
				int newMoveCostNeighbour = parentNode.gCost + getDistance(parentNode, neighbour);
	
				if (openSet.isEmpty() || newMoveCostNeighbour < neighbour.gCost || !openSet.contains(neighbour)) {
					neighbour.gCost = newMoveCostNeighbour;
					neighbour.hCost = getDistance(neighbour, targetNode);
					neighbour.parent = parentNode;
					
//					if (!openSet.contains(neighbor)) {
					if (openSet.isEmpty() || !openSet.contains(neighbour)) {
						openSet.add(neighbour);
					}
					else {
						openSet.updateItem(neighbour);
					}
				}
			}
			
		}
	}

	public ArrayList<Node> retracePath(Node startNode, Node endNode) {
		Node currentNode = endNode;
	    ArrayList<Node> path = new ArrayList<Node>();
		while (currentNode != startNode && currentNode != null) {
			currentNode.inPath = true;
			path.add(currentNode);
			currentNode = currentNode.parent;
		}
		return path;
	}

	public void move(String direction) {
		// Direction may be: N,S,W,E,NE,NW,SE,SW
		// move the boat 1 cell in the required direction
		int i = getBoat().gridY;
		int j = getBoat().gridX;
		
		ArrayList<Node> openings = getNeighbours(getBoat());
		for (Node b : openings) {
			if (!b.walkable) {
				continue;
			}
			if (direction.equals("N") && b.gridY == (i-1) && b.gridX == j) {
				this.boat = b;
			}
			else if (direction.equals("S") && b.gridY == (i+1) && b.gridX == j) {
				this.boat = b;
			}
			else if (direction.equals("W") && b.gridY == (i) && b.gridX == (j-1)) {
				this.boat = b;
			}
			else if (direction.equals("E") && b.gridY == (i) && b.gridX == (j+1)) {
				this.boat = b;
			}
			else if (direction.equals("NE") && b.gridY == (i-1) && b.gridX == (j+1)) {
				this.boat = b;
			}
			else if (direction.equals("NW") && b.gridY == (i-1) && b.gridX == (j-1)) {
				this.boat = b;
			}
			else if (direction.equals("SE") && b.gridY == (i+1) && b.gridX == (j+1)) {
				this.boat = b;
			}
			else if (direction.equals("SW") && b.gridY == (i+1) && b.gridX == (j-1)) {
				this.boat = b;
			}
		}
            
	}
	
	public Node getTreasure(int range) {
		// range is the range of the sonar
		// if the distance of the treasure from the boat is less or equal that the sonar range,
		// return the treasure node. Otherwise return null.
		if (getDistance(getBoat(), treasure) <= range) {
			return treasure;
		}
		else {
			return null;
		}
	}

}
