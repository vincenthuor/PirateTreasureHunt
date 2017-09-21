package heap;

public class Heap<T extends HeapItem> {

	protected T[] items; // Array that is used to store heap items. items[0] is the highest priority element.
	protected int maxHeapSize; // The capacity of the heap
	protected int currentItemCount; // How many elements we have currently on the heap

	public Heap(int maxHeapSize) {
		this.maxHeapSize = maxHeapSize;
		items = (T[]) new HeapItem[maxHeapSize];
		currentItemCount = 0; // heap is empty!
	}

	public boolean isEmpty() {
		return currentItemCount == 0;
	}

	public boolean isFull() {
		return currentItemCount == maxHeapSize;
	}

	public void add(T item) throws HeapFullException
	// Adds item T to its correct position on the heap
	{
		if (isFull())
			throw new HeapFullException();
		else {
			item.setHeapIndex(currentItemCount);
			items[currentItemCount] = item;  // the element is added to the bottom
			sortUp(item); // Move the element up to its legitimate place
			currentItemCount++;
		}
	}

	public boolean contains(T item)
	// Returns true if item is on the heap
	// Otherwise returns false
	{
		return items[item.getHeapIndex()].equals(item);
	}

	public int count() {
		return currentItemCount;
	}

	public void updateItem(T item) {
		sortUp(item);
	}

	public T removeFirst() throws HeapEmptyException
	// Removes and returns the element sitting on top of the heap
	{
		if (isEmpty())
			throw new HeapEmptyException();
		else {
			T firstItem = items[0]; // element of top of the heap is stored in firstItem variable
			currentItemCount--;
			items[0] = items[currentItemCount]; //last element moves on top
			items[0].setHeapIndex(0);
			sortDown(items[0]); // move the element to its legitimate position
			return firstItem;
		}
	}
	
	private void sortUp(T item) {
		int k = item.getHeapIndex();
		
		while (k > 0) {
			int p = (k-1)/2;
			T newItem = items[k];
			T parent = items[p];
			if (newItem.compareTo(parent) < 0) {
				// swap Object positions
		        T tmp = items[p];
		        items[p] = items[k];
		        items[k] = tmp;
		        
		        // HeapIndex "swap"
		        items[p].setHeapIndex(p);
		        items[k].setHeapIndex(k);
		        
				// next
				k = p;
			} 
			else {
				break;
			}
		}	
	}
	
	private void sortDown(T item) {
		items[currentItemCount] = null;
		int k = item.getHeapIndex(); // 0
		int leftChild = 2*k+1;
		while (leftChild < currentItemCount) {
			int min = leftChild, rightChild = leftChild + 1;
			if (rightChild < currentItemCount) { // there is a right child
				if (items[rightChild].compareTo(items[leftChild]) < 0) {
					min++;
				}
			}
			if (items[k].compareTo(items[min]) > 0) {
				// swap Object positions
				T temp = items[k];
				items[k] = items[min];
				items[min]= temp;

			    // HeapIndex "swap"
			    items[k].setHeapIndex(k);
			    items[min].setHeapIndex(min);
			    
			    // next
			    k = min;
				leftChild = 2*k+1;		
			} 
			else {
				break;
			}
		}
		
	}
}
