package sequential;

public class FourSweep {
	Graph graph;
	PathFinder finder;

	public FourSweep(Graph graph) {
		this.graph = graph;
	}

	public Result run() {

		long lowerb = 0;
		int source = getRandomSource();
		String[] bfsResult = runBFS(source);
		int a1 = Integer.parseInt(bfsResult[0]);
		int best = Integer.parseInt(bfsResult[1]);

		if (lowerb < best) {
			lowerb = best;
		}

		bfsResult = runBFS(a1);
		int b1 = Integer.parseInt(bfsResult[0]);
		best = Integer.parseInt(bfsResult[1]);

		if (lowerb < best) {
			lowerb = best;
		}

		int r2 = finder.getMiddle(b1);
		bfsResult = runBFS(r2);
		int a2 = Integer.parseInt(bfsResult[0]);
		best = Integer.parseInt(bfsResult[1]);

		if (lowerb < best) {
			lowerb = best;
		}

		bfsResult = runBFS(a2);
		int b2 = Integer.parseInt(bfsResult[0]);
		best = Integer.parseInt(bfsResult[1]);

		if (lowerb < best) {
			lowerb = best;
		}
		int u = finder.getMiddle(b2);
		Result res = new Result(lowerb, u);

		return res;
	}

	private int getRandomSource() {
		int n = graph.V();
		int r1 = (int) (Math.random() * n);
		int i = 0;
		int source = -1;
		for (int s : graph.vertices()) {
			if (i == r1) {
				source = s;
				break;
			}
			i++;
		}
		System.out.println("Random source is " + source);
		return source;
	}

	private String[] runBFS(int source) {
		int best = -1;
		int destination = -1;
		finder = new PathFinder(graph, source);
		for (int v : graph.vertices()) {
			if (finder.hasPathTo(v) && finder.distanceTo(v) > best) {
				best = finder.distanceTo(v);
				destination = v;
			}
		}
		String[] result = { destination + "", best + "" };

		return result;
	}
}
