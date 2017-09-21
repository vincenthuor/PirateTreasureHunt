package world;

import heap.HeapItem;

public class Node implements HeapItem {

	protected boolean walkable; // true if this node is available for passage, false if it belongs to an obstacle (i.e. an island)
	protected int gridX, gridY; // gridX runs left to right starting from 0, gridY runs top to bottom starting from 0
	protected int gCost, hCost; // gCost & hCost as explained in the A-star algorithm
	protected int heapIndex;    // this is used to implement the methods imposed by HeapItem interface
	protected Node parent;      // points to previous node in the calculated path
	protected boolean inPath;   // true if the node belongs to the calculated path, false otherwise

	public Node(boolean walkable, int gridX, int gridY) {
		this.walkable = walkable;
		this.gridX = gridX;
		this.gridY = gridY;
		this.inPath = false;
	}

	public int getFCost() {
		return gCost + hCost;
	}

	public int getHCost() {
		return hCost;
	}

	@Override
	public int compareTo(HeapItem other) {

		if (this.getFCost() > ((Node) other).getFCost()) {
			return 1;
		}
		if (this.getFCost() < ((Node) other).getFCost()) {
			return -1;
		}
		else {
			return 0;
		}
	}

	@Override
	public void setHeapIndex(int index) {
		heapIndex = index;
	}

	@Override
	public int getHeapIndex() {
		return heapIndex;
	}

	@Override
	public boolean equals(Object other) {
		// Two nodes are equal if they occupy same position in the map.
		if (other == null) {
			return false;
		}
		if (other == this) {
			return true;
		}
		if (other.getClass() != this.getClass()) {
			return false;
		}
		return this.gridX == ((Node) other).gridX &&
				this.gridY == ((Node) other).gridY;
	}
}
