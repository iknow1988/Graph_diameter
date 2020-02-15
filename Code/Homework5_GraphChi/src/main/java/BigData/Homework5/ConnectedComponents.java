package BigData.Homework5;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import edu.cmu.graphchi.ChiVertex;
import edu.cmu.graphchi.GraphChiContext;
import edu.cmu.graphchi.GraphChiProgram;
import edu.cmu.graphchi.datablocks.IntConverter;
import edu.cmu.graphchi.engine.GraphChiEngine;
import edu.cmu.graphchi.engine.VertexInterval;
import edu.cmu.graphchi.util.IdCount;
import edu.cmu.graphchi.util.LabelAnalysis;
import edu.cmu.graphchi.vertexdata.ForeachCallback;
import edu.cmu.graphchi.vertexdata.VertexAggregator;

public class ConnectedComponents implements GraphChiProgram<Integer, Integer> {
	// private static Logger logger = ChiLogger.getLogger("BFS");

	@Override
	public void update(ChiVertex<Integer, Integer> vertex,
			GraphChiContext context) {
		final int iteration = context.getIteration();
		final int numEdges = vertex.numEdges();
		if (iteration == 0) {
			vertex.setValue(vertex.getId());
			context.getScheduler().addTask(vertex.getId());
		}

		int curMin = vertex.getValue();
		for (int i = 0; i < numEdges; i++) {
			int nbLabel = vertex.edge(i).getValue();
			if (iteration == 0)
				nbLabel = vertex.edge(i).getVertexId(); // Note!
			if (nbLabel < curMin) {
				curMin = nbLabel;
			}
		}

		vertex.setValue(curMin);
		int label = curMin;

		if (iteration > 0) {
			for (int i = 0; i < numEdges; i++) {
				if (vertex.edge(i).getValue() > label) {
					vertex.edge(i).setValue(label);
					context.getScheduler()
							.addTask(vertex.edge(i).getVertexId());
				}
			}
		} else {
			for (int i = 0; i < vertex.numOutEdges(); i++) {
				vertex.outEdge(i).setValue(label);
			}
		}
	}

	@Override
	public void beginIteration(GraphChiContext ctx) {

	}

	@Override
	public void endIteration(GraphChiContext ctx) {

	}

	@Override
	public void beginInterval(GraphChiContext ctx, VertexInterval interval) {
	}

	@Override
	public void endInterval(GraphChiContext ctx, VertexInterval interval) {
	}

	@Override
	public void beginSubInterval(GraphChiContext ctx, VertexInterval interval) {
	}

	@Override
	public void endSubInterval(GraphChiContext ctx, VertexInterval interval) {
	}

	public static ArrayList<Integer> start(
			GraphChiEngine<Integer, Integer> engine, String baseFilename)
			throws IOException {
		ArrayList<Integer> list = new ArrayList<Integer>();
		engine.setEdataConverter(new IntConverter());
		engine.setVertexDataConverter(new IntConverter());
		//engine.setSkipZeroDegreeVertices(true);
		engine.setEnableScheduler(true);
		engine.run(new ConnectedComponents(), 10);

		/* Process output. The output file has format <vertex-id, component-id> */
		LabelAnalysis.computeLabels(baseFilename, engine.numVertices(),
				engine.getVertexIdTranslate());
		final HashMap<Integer, IdCount> counts = new HashMap<Integer, IdCount>(
				1000000);
		VertexAggregator.foreach(engine.numVertices(), baseFilename,
				new IntConverter(), new ForeachCallback<Integer>() {
					public void callback(int vertexId, Integer vertexValue) {
						if (vertexId != vertexValue) {
							IdCount cnt = counts.get(vertexValue);
							if (cnt == null) {
								cnt = new IdCount(vertexValue, 1);
								counts.put(vertexValue, cnt);
							}
							cnt.count++;
						}
					}
				});

		for (IdCount cnt : counts.values()) {
			int s = engine.getVertexIdTranslate().backward(cnt.id);
			list.add(s);
		}
		return list;
	}
}
