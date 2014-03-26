/*************************************************************
 * Project: Avatar Demo
 * Class: avatar.core.utils.mapmatching.MReport
 * Author: Huajian Mao
 * Purpose: ...
 * LastChanged: 2011-12-28 14:52:05
 * Email: huajianmao@gmail.com
*************************************************************/
package avatar.core.utils.mapmatching;

import java.util.PriorityQueue;

import avatar.core.model.mobject.Report;

public class MReport extends Report {
	private Candidate likely;
	private boolean isPinned;

	private PriorityQueue<Candidate> candidates;

	public MReport(Report report) {
		this.id = report.getId();
		this.lpn = report.getLpn();
		this.point = report.getPoint();
		this.speed = report.getSpeed();
		this.angle = report.getAngle();
		this.timestamp = report.getTimestamp();
		this.status = report.getStatus();

		this.likely = null;
		this.isPinned = false;
		this.candidates = null;
	}

	public Candidate getLikely() {
		return likely;
	}

	public void setLikely(Candidate likely) {
		this.likely = likely;
	}

	public PriorityQueue<Candidate> getCandidates() {
		return candidates;
	}

	public void setCandidates(PriorityQueue<Candidate> candidates) {
		this.candidates = candidates;
	}

	public boolean isPinned() {
		return isPinned;
	}

	public void setPinned(boolean isPinned) {
		this.isPinned = isPinned;
	}

	public int compareTo(Report other) {
		int rst;
		long temp = this.getTimestamp() - other.getTimestamp();
		if (temp > 0)
			rst = 1;
		else if (temp == 0)
			rst = 0;
		else
			rst = -1;
		return rst;
	}
}
