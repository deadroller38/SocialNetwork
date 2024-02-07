package graph;

class Node implements Comparable<Node> {

	private int name;
	private int edges;
	
	public Node (int n, int e) {
		name = n;
		edges = e;
	}
	
	@Override
	public int compareTo(Node other) {
		Integer e1 = this.getNumEdges();
		Integer e2 = other.getNumEdges();
		if (e1 > e2) {
			return -1;
		} else if (e1 < e2) {
			return 1;
		}
		return 0;
	}
	
	int getName() {
		return name;
	}
	
	int getNumEdges() {
		return edges;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Node)) {
			return false;
		}
		
		Node n = (Node) o;
		if (this.name == n.getName()) {
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return Integer.toString(name);
	}
}
