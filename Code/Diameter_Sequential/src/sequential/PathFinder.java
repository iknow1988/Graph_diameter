package sequential;

import java.util.ArrayList;

public class PathFinder {

	private ST<Integer, Integer> prevPath = new ST<Integer, Integer>();
	private ST<Integer, Integer> dist = new ST<Integer, Integer>();

	public PathFinder(Graph G, int s) {
		Queue<Integer> q = new Queue<Integer>();
		q.enqueue(s);
		dist.put(s, 0);

		while (!q.isEmpty()) {
			int v = q.dequeue();
			for (int w : G.adjacentTo(v)) {
				if (!dist.contains(w)) {
					q.enqueue(w);
					dist.put(w, 1 + dist.get(v));
					prevPath.put(w, v);
				}
			}
		}
	}

	public boolean hasPathTo(int v) {
		return dist.contains(v);
	}

	public int distanceTo(int v) {
		if (!hasPathTo(v))
			return Integer.MAX_VALUE;
		return dist.get(v);
	}

	public Iterable<Integer> pathTo(Integer v) {
		Stack<Integer> path = new Stack<Integer>();
		while (v != null && dist.contains(v)) {
			path.push(v);
			v = prevPath.get(v);
		}
		return path;
	}

	public int getMiddle(Integer v) {
		int middle = -1;
		Stack<Integer> path = new Stack<Integer>();
		ArrayList<Integer> mid = new ArrayList<Integer>();
		while (v != null && dist.contains(v)) {
			path.push(v);
			mid.add(v);
			v = prevPath.get(v);
		}
		middle = mid.get((int) Math.floor(mid.size() / 2));
		return middle;
	}

	public String getVertex(int distance) {
		String result = null;
		result = dist.getKey(distance);
		return result;
	}
}