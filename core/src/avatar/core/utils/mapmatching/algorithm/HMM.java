/*************************************************************
 * Project: Avatar Demo
 * Class: avatar.core.utils.mapmatching.algorithm.HMM
 * Author: Huajian Mao
 * Purpose: ...
 * LastChanged: 2011-12-28 14:52:05
 * Email: huajianmao@gmail.com
*************************************************************/
package avatar.core.utils.mapmatching.algorithm;

import java.util.ArrayList;
import java.util.PriorityQueue;

import avatar.core.model.geometry.Path;
import avatar.core.model.geometry.Region;
import avatar.core.model.mobject.Report;
import avatar.core.model.mobject.Trace;
import avatar.core.model.mobject.Trajectory;
import avatar.core.utils.common.Constant;
import avatar.core.utils.common.Distribution;
import avatar.core.utils.common.Logging;
import avatar.core.utils.common.Measure;
import avatar.core.utils.mapmatching.BestPrev;
import avatar.core.utils.mapmatching.Candidate;
import avatar.core.utils.mapmatching.MMBase;
import avatar.core.utils.mapmatching.MMIface;
import avatar.core.utils.mapmatching.MReport;
import avatar.core.utils.route.Route;

public class HMM extends MMBase implements MMIface {

	public HMM(Region region, Route route) {
		super(region, route);
	}

//	public String estimate (Trace trace) {
//		ArrayList<Match> matches = null;
//		String content = "";
//		try {
//			matches = setupCandidates(trace, Constant.MAX_CAND_NUM);
//			for (Match match: matches) {
//				int start = 0;
//				int end = scoreCandidates(match.getTrace(), start);
//				setLikely(match.getTrace(), start, end);
//
//				List<VertexTime> vertices = estimateVertexTime(match);
//				for (VertexTime vertexTime: vertices) {
//					content += vertexTime + "\n";
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return content;
//	}
	@Override
	public Trajectory map(Trace trace) {
		Trajectory traj = null;
		if (trace==null || trace.getReports() == null || trace.getReports().size() == 0) {
			return null;
		}
		try {
			traj = setupCandidates(trace, Constant.MAX_CAND_NUM);
			int start = 0;
			int end = scoreCandidates(traj.getTrace(), start);
			setLikely(traj.getTrace(), start, end);
			getBestRoute(traj);
			traj.setTraceID(trace.getTraceID());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return traj;
	}
	
	private int scoreCandidates(Trace trace, int start) throws Exception {
		int index = start;
		ArrayList<Report> reports = trace.getReports();
		int repCnt = reports.size();
		if (start >= repCnt) {
			return -1;
		}

		MReport thisRep, prevRep;
		PriorityQueue<Candidate> prevCands, thisCands;

		for (index = start + 1; index < repCnt; index++) {
			prevRep = (MReport) reports.get(index - 1);
			thisRep = (MReport) reports.get(index);
			prevCands = prevRep.getCandidates();
			thisCands = thisRep.getCandidates();

			double repsDist = Measure.distance(prevRep.getPoint(), thisRep
					.getPoint());
//			double maxPathLength = 1.0
//					* (thisRep.getTimestamp() - prevRep.getTimestamp()) / 1000
//					* Constant.MAX_SPEED / 3.6;

			// FIXME: 
			if (thisCands == null) {
				break;
			}
			Candidate maxScoreCand = null;
			for (Candidate thisCand : thisCands) {
				double spatialScore = getSpatialScore(thisCand.getDistance());
				thisCand.setScore(0);
				for (Candidate prevCand : prevCands) {
					double prevCandScore = prevCand.getScore();
					Path path = this.getPath(prevCand, thisCand, Double.POSITIVE_INFINITY);
					if (path == null || path.getLength() == -1) {
						Logging.debug("Cannot find the path between " + prevCand.getcPoint().getRoad().getId() + " to " + thisCand.getcPoint().getRoad().getId());
						continue;
					}
					double pathScore = getPathScore(path, repsDist);

					double alt = prevCandScore + spatialScore * pathScore;

					// if alt has reached the low bound then this step can be skipped.
					if (alt > thisCand.getScore()) {
						thisCand.setScore(alt);
						BestPrev bestPrev = new BestPrev();
						bestPrev.setBestPrevCand(prevCand);
						bestPrev.setBestPrevPath(path);
						thisCand.setBestPrev(bestPrev);

						if (maxScoreCand==null || (maxScoreCand!=null && alt>maxScoreCand.getScore())) {
							maxScoreCand = thisCand;
						}
					}
				}
			}

			// FIXME: as priorityQueue does not provide the order update function,
			//        we need to update the order manually.
			//        We should try some other more efficient methods.
			if (maxScoreCand != null) {
				thisCands.remove(maxScoreCand);
				thisCands.add(maxScoreCand);
			}
//			PriorityQueue<Candidate> tempCands = new PriorityQueue<Candidate>(Constant.DEFAULT_QUEUE_SIZE, Collections.reverseOrder());
//			for (int i=0; i< thisCands.size(); i++) {
//				Candidate cand = thisCands.poll();
//				tempCands.add(cand);
//			}
//			thisRep.setCandidates(tempCands);
		}
		index--;
		return index;
	}

	protected double getSpatialScore(double distance) {
		double score = Distribution.normal(distance,
				Constant.NORMAL_DISTANCE_ERROR_MEAN,
				Constant.NORMAL_DISTANCE_ERROR_DEVIATION);
		return score;
	}

	protected double getPathScore(Path path, double repsDist) {
		double score = -1;
		if (path != null) {
			double beta, delta;

			beta = 50 / Math.log(2);
			delta = Math.abs(repsDist - path.getLength());
			score = Math.exp(-delta / beta) / beta;
		}
		return score;
	}

	private int setLikely(Trace trace, int start, int end) {
		ArrayList<Report> reports = trace.getReports();
		if (end >= reports.size()) {
			end = reports.size()-1;
		}
		Candidate likely;
		MReport thisRep;

		int index = end;
		thisRep = (MReport) reports.get(index);

		likely = thisRep.getCandidates().peek();
		thisRep.setLikely(likely);
		for (int i = end-1; i >= start; i--) {
			if (likely == null)
				break;
			BestPrev bestPrev = likely.getBestPrev();
			if (bestPrev != null) {
				likely = bestPrev.getBestPrevCand();
				thisRep = (MReport) reports.get(i);
				thisRep.setLikely(likely);
			}
		}

		return index;
	}
}
