package sequential;

public class Result {
	private long lb;
	private int u;

	public Result(long lowerb, int u) {
		this.lb = lowerb;
		this.u = u;
	}

	public Result() {
	}

	public long lb() {
		return this.lb;
	}

	public int getU() {
		return this.u;
	}
}
