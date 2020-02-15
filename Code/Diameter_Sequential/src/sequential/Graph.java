package sequential;

public class Graph {

	// symbol table: key = string vertex, value = set of neighboring vertices
	private ST<Integer, SET<Integer>> st;

	// number of edges
	private int E;

	/**
	 * Create an empty graph with no vertices or edges.
	 */
	public Graph() {
		st = new ST<Integer, SET<Integer>>();
	}

	/**
	 * Create an graph from given input stream using given delimiter.
	 */
	public Graph(In in, String delimiter) {
		st = new ST<Integer, SET<Integer>>();
		while (in.hasNextLine()) {
			String line = in.readLine();
			if (line.startsWith("#"))
				continue;
			String[] names = line.split(delimiter);
			for (int i = 1; i < names.length; i++) {
				int source = Integer.parseInt(names[0].trim());
				int dest = Integer.parseInt(names[i].trim());
				addEdge(source, dest);
			}
		}
	}

	/**
	 * Number of vertices.
	 */
	public int V() {
		return st.size();
	}

	/**
	 * Number of edges.
	 */
	public int E() {
		return E;
	}

	// throw an exception if v is not a vertex
	private void validateVertex(int v) {
		if (!hasVertex(v))
			throw new IllegalArgumentException(v + " is not a vertex");
	}

	/**
	 * Degree of this vertex.
	 */
	public int degree(int v) {
		validateVertex(v);
		return st.get(v).size();
	}

	/**
	 * Add edge v-w to this graph (if it is not already an edge)
	 */
	public void addEdge(int v, int w) {
		if (!hasVertex(v))
			addVertex(v);
		if (!hasVertex(w))
			addVertex(w);
		if (!hasEdge(v, w)) {
			E++;
			st.get(v).add(w);
			st.get(w).add(v);
		}
	}

	/**
	 * Add vertex v to this graph (if it is not already a vertex)
	 */
	public void addVertex(int v) {
		if (!hasVertex(v))
			st.put(v, new SET<Integer>());
	}

	/**
	 * Return the set of vertices as an Iterable.
	 */
	public Iterable<Integer> vertices() {
		return st;
	}

	/**
	 * Return the set of neighbors of vertex v as in Iterable.
	 */
	public Iterable<Integer> adjacentTo(int v) {
		validateVertex(v);
		return st.get(v);
	}

	/**
	 * Is v a vertex in this graph?
	 */
	public boolean hasVertex(int v) {
		return st.contains(v);
	}

	/**
	 * Is v-w an edge in this graph?
	 */
	public boolean hasEdge(int v, int w) {
		validateVertex(v);
		validateVertex(w);
		return st.get(v).contains(w);
	}

	/**
	 * Return a string representation of the graph.
	 */
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (Integer v : st) {
			s.append(v + ": ");
			for (int w : st.get(v)) {
				s.append(w + " ");
			}
			s.append("\n");
		}
		return s.toString();
	}
}