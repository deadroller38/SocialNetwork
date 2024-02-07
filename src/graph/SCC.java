package graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * The different class for SCC because we don't need methods getEgonet and some others for SCC.
 */
public class SCC implements Graph {

	private HashMap<Integer, HashSet<Integer>> myGraph;
	
	
	public SCC() {
		myGraph = new HashMap<Integer, HashSet<Integer>>();
	}

	@Override
	public void addVertex(int num) {
		if (!myGraph.containsKey(num)) {
			myGraph.put(num, new HashSet<Integer>());
		}
	}

	@Override
	public void addEdge(int from, int to) {
		if (!myGraph.containsKey(from) || !myGraph.containsKey(to)) {
			throw new NullPointerException("Stard or finish vertex doesn't exist");
		}
		HashSet<Integer> cur = myGraph.get(from);
		if (!cur.contains(to)) {
			cur.add(to);
			myGraph.put(from, cur);
		}
	}

	@Override
	public Graph getEgonet(int center) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Graph> getSCCs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<Integer, HashSet<Integer>> exportGraph() {
		return myGraph;
	}

	public Set<Integer> getVertices() {
		Set<Integer> vertices = myGraph.keySet();
		return vertices;
	}
}
