package sequential;

public class IFub {
	public static long[] run(Graph graph, int k) {
		Result fourSweepresult = new Result();
		long startTime = System.currentTimeMillis();
		FourSweep fourSweep = new FourSweep(graph);
		fourSweepresult = fourSweep.run();
		long l = fourSweepresult.lb();
		int u = fourSweepresult.getU();
		int vis = 4;
		int i = -1;

		PathFinder finder = new PathFinder(graph, u);
		for (int v : graph.vertices()) {
			if (finder.hasPathTo(v) && finder.distanceTo(v) > i) {
				i = finder.distanceTo(v);
			}
		}
		vis++;
		int lb = (int) l;
		if (i > lb) {
			lb = i;
		}
		int ub = 2 * i;
		
		while ((ub - lb) > k) {
			int Biu = lb;
			for (int j : graph.vertices()) {
				if (finder.distanceTo(j) == i) {
					PathFinder finder2 = new PathFinder(graph, j);
					vis++;

					int eccj = -1;
					for (int v : graph.vertices()) {
						if (finder2.hasPathTo(v)
								&& finder2.distanceTo(v) > eccj) {
							eccj = finder2.distanceTo(v);
						}
					}
					if (eccj > Biu) {
						Biu = eccj;
					}
					if (Biu == ub) {
						break;
					}
				}
			}
			// If the current maximum eccentricity is greater than 2(i-1),
			// then
			// we have found the diameter
			if (Biu > 2 * (i - 1)) {
				ub = Biu;
				lb = Biu;
				break;
			} else {
				// Otherwise we update the lower and the upper bound
				lb = Biu;
				ub = 2 * (i - 1);
			}
			i = i - 1;
		}
		long[] rst = new long[4];
		rst[0] = lb;
		rst[1] = ub;
		rst[2] = vis;
		long endTime = System.currentTimeMillis();
		rst[3] = endTime - startTime;
		return rst;
	}

}
