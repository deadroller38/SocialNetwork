/**
 * 
 */
package graph;

import java.util.*;


/**
 * @author Your name here.
 * 
 * For the warm up assignment, you must implement your Graph in a class
 * named CapGraph.  Here is the stub file.
 *
 */
public class CapGraph implements Graph {

	/* (non-Javadoc)
	 * @see graph.Graph#addVertex(int)
	 */
	private HashMap<Integer, HashSet<Integer>> myGraph;
	
	public CapGraph() {
		myGraph = new HashMap<Integer, HashSet<Integer>>();
	}
	
	@Override
	public void addVertex(int num) {
		// TODO Auto-generated method stub
		if (!myGraph.containsKey(num)) { 					//check if vertex is not in the Graph
			myGraph.put(num, new HashSet<Integer>());
		}
	}

	/* (non-Javadoc)
	 * @see graph.Graph#addEdge(int, int)
	 */
	@Override
	public void addEdge(int from, int to) {
		// TODO Auto-generated method stub
		if (!myGraph.containsKey(from) || !myGraph.containsKey(to)) {
			throw new NullPointerException("Stard or finish vertex doesn't exist");
		}
		HashSet<Integer> cur = myGraph.get(from);  // get current neighbors of vertex from
		if (!cur.contains(to)) {
			cur.add(to);             				// add vertex to to neighbors
			myGraph.put(from, cur);
		}
	}

	/* (non-Javadoc)
	 * @see graph.Graph#getEgonet(int)
	 */
	@Override
	public Graph getEgonet(int center) {
		// TODO Auto-generated method stub
		if (!myGraph.containsKey(center)) {
			return new CapGraph();
		}
		CapGraph ego = new CapGraph();				// create new Graph to keep Egonet
		ego.addVertex(center);						// add vertex center to Egonet
		for (int neighbor : myGraph.get(center)) {
			ego.addVertex(neighbor);
			ego.addEdge(center, neighbor);
			for (int nextNeighbor : myGraph.get(neighbor)) {
				if (myGraph.get(center).contains(nextNeighbor) || nextNeighbor == center) {
					ego.addVertex(nextNeighbor);
					ego.addEdge(neighbor, nextNeighbor);
				}
			}
		}
		return ego;
	}

	/* (non-Javadoc)
	 * @see graph.Graph#getSCCs()
	 */
	@Override
	public List<Graph> getSCCs() {
		
		List<Graph> SCCs = new ArrayList<Graph>(); // create list to keep SCCs
		// create queue and stack all vertexes
		LinkedList<Integer> vertices = new LinkedList<Integer>(); 
		for (int v : myGraph.keySet()) {
			vertices.add(v);
		}
		
		LinkedList<Integer> finished = DFS(this, vertices); // create stack of visited vertexes in original graph
		vertices = new LinkedList<Integer>(finished); // create copy of list finished
		CapGraph tGraph = transpose(myGraph); // create new graph with opposite directions of edges
		LinkedList<Integer> tfinished = DFS(tGraph, vertices); // create stack of visited vertex with opposite directions of edges so we know that there are return paths
		
		int last = tfinished.removeLast(); // pop the last vertex from the stack
		HashSet<Integer> completed = new HashSet<Integer>(); // we will add all vertexes that we will visit finding SCCs
		while (!finished.isEmpty() && !tfinished.isEmpty()) { // loop for adding SCCs
			int i = finished.pop(); // pop the first vertex from stack
			SCC nSCC = new SCC(); // create new graph as SCC
			if (!completed.contains(i)) {
				nSCC.addVertex(i);
				completed.add(i);
				while (!tfinished.isEmpty()) { //pop every vertex from the end till we find vertex i from finished - all these vertexes are SCC
					if (!nSCC.getVertices().contains(last) && !completed.contains(last)) {
						nSCC.addVertex(last);
						completed.add(last);
					}
					if (i == last) {
						break;
					}
					last = tfinished.removeLast();
				}
			}
			if(!nSCC.exportGraph().isEmpty()) {
				SCCs.add(nSCC);
			}
		}
		return SCCs;
	}
	
	
	/**
	 * Start DFS
	 * @param g - Graph for search
	 * @param vertices - queue of vertex
	 * @return stack of vertices
	 */
	private LinkedList<Integer> DFS(CapGraph g, LinkedList<Integer> vertices) {
		LinkedList<Integer> finished = new LinkedList<Integer>();
		HashSet<Integer> visited = new HashSet<Integer>();
		while (!vertices.isEmpty()) {
			int v = vertices.pop();
			if (!visited.contains(v)) {
				DFSVisit(g, v, visited, finished);
			}
		}
		return finished;
	}
	
	/**
	 * Process DFS
	 * @param g - Graph for search
	 * @param v - current vertex as start point for search
	 * @param visited - set of already visited vertexes
	 * @param finished - stack of vertices that we are filling
	 */
	private void DFSVisit(CapGraph g, int v, HashSet<Integer> visited, LinkedList<Integer> finished) {
		HashMap<Integer, HashSet<Integer>> graph = g.exportGraph();
		visited.add(v);
		for (int neighbor : graph.get(v)) {
			if (!visited.contains(neighbor)) {
				DFSVisit(g, neighbor, visited, finished); // use recursion to process DFS
			}
		}
		finished.push(v);
	}

	/**
	 * Method to change direction of edges in original graph
	 * @param g - original graph
	 * @return new graph with opposite direction of edges in comparison with original graph
	 */
	private CapGraph transpose(HashMap<Integer, HashSet<Integer>> g) {
		CapGraph cGraph = new CapGraph();
		for (int v : myGraph.keySet()) {
			cGraph.addVertex(v);
			for (int n : myGraph.get(v)) {
				cGraph.addVertex(n);
				cGraph.addEdge(n, v);
			}
		}
		return cGraph;
	}
	
	/* (non-Javadoc)
	 * @see graph.Graph#exportGraph()
	 */
	@Override
	public HashMap<Integer, HashSet<Integer>> exportGraph() {
		// TODO Auto-generated method stub
		return myGraph;
	}
	/**
	 * 
	 * @return vertices
	 */
	public Set<Integer> getVertices() {
		Set<Integer> vertices = myGraph.keySet();
		return vertices;
	}

	/**
	 * Method to create list of vertexes (as class Node) sorted  from highest amount of edges to the lowest if order is true or opposite
	 * @param order
	 * @return if order = true - sorted vertexes from highest number of edges to lowest; if order = false - return only vertexes with no or only 1 edge.
	 */
	public List<Node> getSortedKeys(boolean order) {
		List<Node> sortedKeys = new ArrayList<Node>();
		if (order == true) {
			for (int k : myGraph.keySet()) {
				Node n = new Node(k, myGraph.get(k).size());
				sortedKeys.add(n);
			}
			Collections.sort(sortedKeys);
		} /*else {
			for (int k : myGraph.keySet()) {
				if (myGraph.get(k).size() < 2) {
					Node n = new Node(k, myGraph.get(k).size());
					sortedKeys.add(n);
				}
			}
		}*/
		return sortedKeys;
	}
	
	/**
	 * Return dominating set using greedy algorithm
	 * @return
	 */
	public Set<Integer> getDominatingSet() {
		if (myGraph.isEmpty()) {
			return null;
		}
		Set<Integer> DM = new HashSet<Integer>();
		List<Node> sortedKeys = getSortedKeys(true);
		Set<Integer> visited = new HashSet<Integer>();
		for (Node n : sortedKeys) {
			int start = n.getName();
			DM = visitNodes(DM, visited, start);
		}
		if (visited.size() == myGraph.keySet().size()) {
			return DM; // return dominating set only if we visited all vertexes in the graph
		}
		return null;
	}
	
	/**
	 * return set of vertex that are neighbors to vertexes with the only one edge or no edge
	 * @return
	 */
	public Set<Integer> visitCorners(Set<Integer> DM, List<Node> sortedKeys, Set<Integer> visited) {
		if (myGraph.isEmpty()) {
			return null;
		}
		
		int i = sortedKeys.size()-1;
		Node c = sortedKeys.get(i);
		while (c.getNumEdges() < 2) {
			int k = c.getName();
			Integer[] e = new Integer[1];
			int start = myGraph.get(k).toArray(e)[0];
			DM = visitNodes(DM, visited, start);
			//sortedKeys.remove(i);
			i -= 1;
			c = sortedKeys.get(i);
		}

		return DM;
	}
	
	
	
	/**
	 * helper method to add vertexes to dominating set and neighbors to visited set
	 * @param DM - current stage of dominating set
	 * @param visited set with already visited vertexes incl. neighbors
	 * @param start - center vertex
	 * @return updated dominating set
	 */
	private Set<Integer> visitNodes(Set<Integer> DM, Set<Integer> visited, int start) {
		if (!visited.contains(start)) {
				visited.add(start);
				DM.add(start);
				for (int e : myGraph.get(start)) {
					if (!visited.contains(e)) {
						visited.add(e);
					}
				}
			}
		return DM;
	}
	
	/**
	 * method to find dominating set adding at first neighbors of vertexes with the only one or no edge, then using greedy algorithm
	 * @return
	 */

	
	public Set<Integer> getDominatingSetWithCorners() {
		if (myGraph.isEmpty()) {
			return null;
		}
		Set<Integer> DM = new HashSet<Integer>();
		List<Node> sortedKeys = getSortedKeys(true);
		Set<Integer> visited = new HashSet<Integer>();
		DM = visitCorners(DM, sortedKeys, visited);
		
		for (Node n : sortedKeys) {
			int start = n.getName();
			DM = visitNodes(DM, visited, start);
		}
		if (visited.size() == myGraph.keySet().size()) {
			return DM; // return dominating set only if we visited all vertexes in the graph
		}
		return null;
	}
	
}
