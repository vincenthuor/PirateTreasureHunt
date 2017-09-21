package heap;

import world.Node;

public class TestHeap {

	public static void main(String[] args) throws HeapFullException, HeapEmptyException {

		Heap<Node> h = new Heap<Node>(10);
//		System.out.println(h);
//		String[] s = "SONAR    SW".split("\\s+");
//		System.out.println(s[1]);
		Node n = new Node(true, 1, 1);
		Node n2 = new Node(true, 1, 99);
		Node n3 = new Node(true, 1, 2 );
		h.add(n);
		h.add(n2);
		h.add(n3);
		System.out.println(n.getHeapIndex());
		System.out.println(n2.getHeapIndex());
		System.out.println(n3.getHeapIndex());
		boolean b = h.contains(n3);
		System.out.println(b);
		h.removeFirst();
		h.removeFirst();
		h.removeFirst();
		System.out.println(n.getHeapIndex());
		System.out.println(n2.getHeapIndex());
		System.out.println(n3.getHeapIndex());
	}

}
