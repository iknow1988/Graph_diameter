package sequential;

public class Diameter {
	private static Graph graph;

	public static void main(String[] args) {
		String inputFile = args[0];
		In data = new In(inputFile);
		//In data = new In("data/CA-CondMat.txt");
		graph = new Graph(data, "\t");
		StdOut.println("Graph is created");
		// normalBFS();
		long startTime = System.currentTimeMillis();
		iFUB(args[1]);
		long endTime = System.currentTimeMillis();
		System.out.println((endTime - startTime) * 1.0 / 1000);
	}

	private static void iFUB(String run) {
		int runs = Integer.parseInt(run);
		long[][] d = new long[runs][];
		for (int e = 0; e < runs; e++) {
			System.out.println("Iteration : " + e);
			d[e] = IFub.run(graph, 0);
		}
		for (int e = 0; e < runs; e++) {
			StdOut.println("Diameter in " + e + " run is " + d[e][0]);
		}
		long sum = 0;
		for (int e = 0; e < runs; e++) {
			StdOut.println("Run " + e + ": " + d[e][2] + " BFSes");
			sum = sum + d[e][2];
		}
		StdOut.println("Average number of BFSes: " + (float) sum / runs);
	}

	@SuppressWarnings("unused")
	private static void normalBFS() {
		int best = -1;
		int source = -1;
		int dest = -1;
		for (int s : graph.vertices()) {
			PathFinder finder = new PathFinder(graph, s);
			for (int v : graph.vertices()) {
				StdOut.println("(Source,Destination) = (" + s + "," + v + ")");
				if (finder.hasPathTo(v) && finder.distanceTo(v) > best) {
					StdOut.println("Path : " + finder.pathTo(v));
					source = s;
					dest = v;
					best = finder.distanceTo(v);
				}
			}
		}
		PathFinder finder = new PathFinder(graph, source);
		StdOut.println("Diameter : " + best);
		StdOut.println("Path : " + finder.pathTo(dest));
	}
}