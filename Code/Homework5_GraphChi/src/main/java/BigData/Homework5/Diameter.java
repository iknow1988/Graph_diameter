package BigData.Homework5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;

import edu.cmu.graphchi.ChiFilenames;
import edu.cmu.graphchi.datablocks.IntConverter;
import edu.cmu.graphchi.engine.GraphChiEngine;
import edu.cmu.graphchi.io.CompressedIO;
import edu.cmu.graphchi.preprocessing.EdgeProcessor;
import edu.cmu.graphchi.preprocessing.FastSharder;
import edu.cmu.graphchi.preprocessing.VertexProcessor;

public class Diameter {
	private static GraphChiEngine<Integer, Integer> engine;
	private static String baseFilename = "data/intermediate";
	private static String inputFile = "data/input";
	private static ArrayList<Result> resultList = new ArrayList<Result>();

	public static void main(String[] args) throws Exception {
		inputFile = args[0];
		convertToUndirectedGraph();
		long startTime = System.currentTimeMillis();
		preprocessGraphChi(args[1]);
		ArrayList<Integer> componentList = findConnectedComponents();
		startBFS(componentList);
		showResult();
		measureDiameter();
		long endTime = System.currentTimeMillis();
		System.out.println("Time took : " + (endTime - startTime) * 1.0 / 1000);
	}

	private static void measureDiameter() {
		Result res = resultList.get(0);
		System.out.println("Top Component is : " + res.getSource() + ","
				+ res.getDestination() + " and Distance is "
				+ res.getDistance());
		int source = res.getDestination();
		engine.setSkipZeroDegreeVertices(false);
		BFS bfs = new BFS(source);
		try {
			engine.run(bfs, 100);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Diameter is : " + bfs.getDiameter());
	}

	private static void showResult() {
		Collections.sort(resultList);
		for (Result res : resultList) {
			System.out.println("Source : " + res.getSource() + "\t"
					+ "Destination : " + res.getDestination() + "\t"
					+ "Distance : " + res.getDistance());
		}
	}

	private static ArrayList<Integer> findConnectedComponents()
			throws IOException {
		return ConnectedComponents.start(engine, baseFilename);
	}

	private static void preprocessGraphChi(String shard) throws IOException {
		int nShards = Integer.parseInt(shard);
		String fileType = "edgelist";

		CompressedIO.disableCompression();
		@SuppressWarnings("rawtypes")
		FastSharder sharder = createSharder(baseFilename, nShards);
		if (!new File(ChiFilenames.getFilenameIntervals(baseFilename, nShards))
				.exists()) {
			sharder.shard(new FileInputStream(new File(baseFilename)), fileType);
		}

		engine = new GraphChiEngine<Integer, Integer>(baseFilename, nShards);
	}

	private static void startBFS(ArrayList<Integer> componentList) {
		for (int id : componentList) {
			engine.setSkipZeroDegreeVertices(false);
			BFS bfs = new BFS(id);
			try {
				engine.run(bfs, 100);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (bfs.getDistantVertex() != -1)
				resultList.add(new Result(id, bfs.getDistantVertex(), bfs
						.getDiameter()));
		}

	}

	@SuppressWarnings("rawtypes")
	private static FastSharder createSharder(String graphName, int numShards)
			throws IOException {
		// logger.info("@Sharder");
		return new FastSharder<Integer, Integer>(graphName, numShards,
				new VertexProcessor<Integer>() {
					public Integer receiveVertexValue(int vertexId, String token) {
						return 0;
					}
				}, new EdgeProcessor<Integer>() {
					public Integer receiveEdge(int from, int to, String token) {
						return (token == null ? 0 : Integer.parseInt(token));
					}
				}, new IntConverter(), new IntConverter());
	}

	private static void convertToUndirectedGraph() {
		ST<String, SET<String>> st = new ST<String, SET<String>>();
		StringBuilder sb = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {

			String line;

			while ((line = br.readLine()) != null) {
				if (line.startsWith("#"))
					continue;
				String[] names = line.split("\t");
				for (int i = 1; i < names.length; i++) {
					String source = names[0].trim();
					String dest = names[i].trim();
					if (!st.contains(source)) {
						st.put(source, new SET<String>());
					}
					if (!st.contains(dest)) {
						st.put(dest, new SET<String>());
					}
					if (!st.get(source).contains(dest)) {
						st.get(source).add(dest);
						st.get(dest).add(source);
						sb.append(source + "\t" + dest + "\n");
						sb.append(dest + "\t" + source + "\n");
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(baseFilename), "utf-8"))) {
			writer.write(sb.toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
