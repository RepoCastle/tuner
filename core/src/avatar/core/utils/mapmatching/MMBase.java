/*************************************************************
 * Project: Avatar Demo
 * Class: avatar.core.utils.mapmatching.MMBase
 * Author: Huajian Mao
 * Purpose: ...
 * LastChanged: 2011-12-28 14:52:07
 * Email: huajianmao@gmail.com
*************************************************************/
package avatar.core.utils.mapmatching;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;

import avatar.core.model.common.Vertex;
import avatar.core.model.geometry.Path;
import avatar.core.model.geometry.Region;
import avatar.core.model.geometry.Road;
import avatar.core.model.mobject.Report;
import avatar.core.model.mobject.Trace;
import avatar.core.model.mobject.Trajectory;
import avatar.core.utils.common.Constant;
import avatar.core.utils.common.Date;
import avatar.core.utils.common.Logging;
import avatar.core.utils.route.Route;

public class MMBase {
	protected Region region;
	protected Route route;

	public MMBase(Region region, Route route) {
		this.region = region;
		this.route = route;
	}

	protected Trajectory setupCandidates(Trace trace, int maxCandNum)
			throws Exception {
		ArrayList<Report> mreports = new ArrayList<Report>();
		ArrayList<Report> reports = trace.getReports();
		int repCnt = reports.size();

		for (int i = 0; i < repCnt; i++) {
			Report report = reports.get(i);
			if (!(report instanceof MReport)) {
				MReport mreport = new MReport(report);
				HashSet<Road> roads = this.region.getNearbyRoads(report.getPoint());
				PriorityQueue<Candidate> candidates = Candidate.getAllCands(report.getPoint(), roads);
				if (maxCandNum > 0) {
					candidates = Candidate.getTopKCands(candidates, maxCandNum);
				}
				if (candidates != null && candidates.size() > 0) {
					Candidate likely = candidates.peek();
					mreport.setCandidates(candidates);
					mreport.setLikely(likely);
				} else {
					Candidate candidate = new Candidate(report.getPoint(), (Road) this.region.getRoads().get(Constant.DEFAULT_ROAD_ID));
					candidates.add(candidate);
					Candidate likely = candidates.peek();
					mreport.setCandidates(candidates);
					mreport.setLikely(likely);
				}
				mreports.add(mreport);
			} else {
				mreports.add((MReport) report);
			}
		}
		Trajectory traj = null;
		traj = new Trajectory(new Trace(trace.getLpn(), mreports), null);
		return traj;
	}
	protected class VertexTime {
		private Vertex vertex;
		private long time;
		protected VertexTime(Vertex vertex, long time) {
			this.vertex = vertex;
			this.time = time;
		}
		public Vertex getVertex() {
			return vertex;
		}
		public long getTime() {
			return time;
		}
		public String toString() {
			String vertexTimeStr = "";
			double longitude = vertex.getPoint().x;
			double latitude = vertex.getPoint().y;
			String vertexID = String.format("V-%f-%f", longitude, latitude);
			vertexTimeStr += vertexID + ", " + Date.mills2str(time);
			return vertexTimeStr;
		}
		public boolean equals(VertexTime other) {
			boolean isEqual = false;
			if (this.time == other.getTime()
					&& this.vertex.getId() == other.getVertex().getId()) {
				isEqual = true;
			}
			return isEqual;
		}
	}
	
	protected void getBestRoute(Trajectory match) {
		Path path = new Path();
		ArrayList<Report> mreports = match.getTrace().getReports();
		int repCnt = mreports.size();
		MReport prevRep = null;
		MReport thisRep = null;
		for (int i = 1; i < repCnt; i++) {
			prevRep = (MReport) mreports.get(i - 1);
			thisRep = (MReport) mreports.get(i);

			// FIXME: should we limit this with a maximum speed?
			double maxPathLength = 1.0
					* (thisRep.getTimestamp() - prevRep.getTimestamp()) / 1000
					* Constant.MAX_SPEED / 3.6;
			
			Candidate startCand = prevRep.getLikely();
			Candidate endCand = thisRep.getLikely();
			// FIXME: check if the algorithm of getPath is right.
			Path tempPath = this.getPath(startCand, endCand, maxPathLength);// maxPathLength);
			
			// FIXME: what if the path does not exist.
			if (tempPath==null || tempPath.getLength()==-1) {
				Logging.warn("WARNING: can't find the path");
			}
			path.append(tempPath);
		}
		path.format();
		match.setPath(path);
	}
	protected Path getPath(Candidate startCand, Candidate endCand,
			double maxLength) {
		if (startCand == null || endCand == null)
			return null;

		Path path = null;
		CPoint startCPoint = startCand.getcPoint();
		CPoint endCPoint = endCand.getcPoint();

		Road startRoad = startCPoint.getRoad();
		Road endRoad = endCPoint.getRoad();

		path = new Path();
		path.setLength(0);

		if (startRoad.equals(endRoad)) {
			path.append(startRoad);
			double length = Math.abs(startCPoint.getDistToHead() - endCPoint.getDistToHead());
			path.setLength(length);
		} else {
			Vertex startV = startRoad.getTail();
			Vertex endV = endRoad.getHead();
			if (!startCPoint.isTail()) {
				path.append(startRoad);
				double length = startRoad.getLength() - startCPoint.getDistToHead();
				path.setLength(length);
			}
			
			Path midPath = this.route.getPath(startV, endV, maxLength);
			
			if (midPath != null) {
				path.append(midPath);
				if (!endCPoint.isHead()) {
					path.append(endRoad);
					double length = path.getLength();
					length = length - endCPoint.getDistToTail();
					path.setLength(length);
				}
			}
		}
		return path;
	}
}

