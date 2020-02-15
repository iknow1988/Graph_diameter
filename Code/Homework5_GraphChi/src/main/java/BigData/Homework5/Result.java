package BigData.Homework5;

public class Result implements Comparable<Result> {
	private int source;
	private int destination;
	private int distance;

	public Result(int source, int destination, int distance) {
		this.source = source;
		this.destination = destination;
		this.distance = distance;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public int getDestination() {
		return destination;
	}

	public void setDestination(int destination) {
		this.destination = destination;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	@Override
	public int compareTo(Result result) {
		if (distance < result.getDistance()) {
			return 1;
		} else if (distance > result.getDistance()) {
			return -1;
		} else {
			return 0;
		}
	}
}
