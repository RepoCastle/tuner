package avatar.core.model.common;

public class TimeWindow {
	private long stime;
	private long etime;

	public TimeWindow(long stime, long etime) {
		this.stime = stime;
		this.etime = etime;
	}

	public long getStime() {
		return stime;
	}

	public void setStime(long stime) {
		this.stime = stime;
	}

	public long getEtime() {
		return etime;
	}

	public void setEtime(long etime) {
		this.etime = etime;
	}

	public boolean contains(long timestamp) {
		boolean contain = false;
		if (this.stime <= timestamp && timestamp <= this.etime) {
			contain = true;
		}
		return contain;
	}
}
