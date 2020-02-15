package BigData.Homework5;

import java.util.logging.Logger;

import edu.cmu.graphchi.ChiLogger;
import edu.cmu.graphchi.ChiVertex;
import edu.cmu.graphchi.GraphChiContext;
import edu.cmu.graphchi.GraphChiProgram;
import edu.cmu.graphchi.engine.VertexInterval;

public class BFS implements GraphChiProgram<Integer, Integer> {
	private static Logger logger = ChiLogger.getLogger("BFS");
	private int id;
	private int best = -1;
	private int distance = 0;

	public BFS(int id) {
		this.id = id;
		logger.info("source is ---------------------------------------------------: " + id);
	}

	public int getDistantVertex() {
		return this.best;
	}

	public int getDiameter() {
		return this.distance;
	}

	@Override
	public void update(ChiVertex<Integer, Integer> vertex,
			GraphChiContext context) {
		boolean converged = true;
		if (context.getIteration() == 0) {
			if (vertex.getId() != id) {
				vertex.setValue(Integer.MAX_VALUE-1);
				for (int i = 0; i < vertex.numOutEdges(); i++) {
					vertex.outEdge(i).setValue(Integer.MAX_VALUE-1);
				}
			} else {
				vertex.setValue(0); // source vertex
				for (int i = 0; i < vertex.numOutEdges(); i++) {
					context.getScheduler().addTask(
							vertex.outEdge(i).getVertexId());
					vertex.outEdge(i).setValue(0);
					// logger.info("neighbors : "
					// + vertex.outEdge(i).getVertexId() + " : value : "
					// + vertex.outEdge(i).getValue());
				}
			}

		} else {
			/* Do computation */
			for (int i = 0; i < vertex.numInEdges(); i++) {
				int inedge_value = vertex.inEdge(i).getValue();
				// logger.info("Computation : " + vertex.getId() + " : "
				// + vertex.inEdge(i).getVertexId() + " : value : "
				// + vertex.inEdge(i).getValue());
				if (inedge_value + 1 < vertex.getValue()) {
					converged = false;
					distance = inedge_value + 1;
					vertex.setValue(distance);
					best = context.getVertexIdTranslate().backward(
							vertex.getId());
				}
			}
			if (!converged) {
				int vertex_data = vertex.getValue();
				for (int j = 0; j < vertex.numOutEdges(); j++) {
					context.getScheduler().addTask(
							vertex.outEdge(j).getVertexId());
					vertex.outEdge(j).setValue(vertex_data);
				}
			}
		}
		// logger.info("@iteration " + context.getIteration() + " : "
		// + vertex.getId() + " : " + vertex.getValue());
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
}
