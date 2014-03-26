/*************************************************************
 * Project: Avatar Demo
 * Class: avatar.core.utils.mapmatching.Candidate
 * Author: Huajian Mao
 * Purpose: ...
 * LastChanged: 2011-12-28 14:52:08
 * Email: huajianmao@gmail.com
*************************************************************/
package avatar.core.utils.mapmatching;

import java.util.Collection;
import java.util.Collections;
import java.util.PriorityQueue;

import avatar.core.model.common.Point;
import avatar.core.model.geometry.Road;
import avatar.core.utils.common.Constant;
import avatar.core.utils.common.Distribution;
import avatar.core.utils.common.Measure;

public class Candidate implements Comparable<Candidate> {
	private CPoint cPoint;

	private double score = -1;
	private double distance = -1;

	private BestPrev bestPrev = null;

	public Candidate(Point point, Road road) {
		this.cPoint = CPoint.getNearestPointOnRoad(point, road);
		this.distance = Measure.distance(point, cPoint);
		this.score = Distribution.normal(this.distance,
				Constant.NORMAL_DISTANCE_ERROR_MEAN,
				Constant.NORMAL_DISTANCE_ERROR_DEVIATION);
		this.bestPrev = null;
	}

	public CPoint getcPoint() {
		return cPoint;
	}

	public double getDistance() {
		if (distance == -1) {
			if (cPoint != null) {
				distance = cPoint.getDistance();
			}
		}
		return distance;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public double getScore() {
		return score;
	}

	public BestPrev getBestPrev() {
		return bestPrev;
	}

	public void setBestPrev(BestPrev bestPrev) {
		this.bestPrev = bestPrev;
	}

	public int compareTo(Candidate other) {
		return Double.compare(this.getScore(), other.getScore());
	}

	public static PriorityQueue<Candidate> getAllCands(Point point, Collection<Road> roads) {
		PriorityQueue<Candidate> totalCands = new PriorityQueue<Candidate>(Constant.DEFAULT_QUEUE_SIZE, Collections.reverseOrder());
		for (Road road : roads) {
			Candidate cand = new Candidate(point, road);
			if (cand.getDistance() < Constant.MAX_DISTANCE_ERROR) {
				totalCands.add(cand);
			}
		}
		return totalCands;
	}

	public static PriorityQueue<Candidate> getCands(Point point, Collection<Road> roads, double maxDist) {
		maxDist = Math.min(maxDist, Constant.MAX_DISTANCE_ERROR);
		PriorityQueue<Candidate> totalCands = new PriorityQueue<Candidate>(Constant.DEFAULT_QUEUE_SIZE, Collections.reverseOrder());
		for (Road road : roads) {
			Candidate cand = new Candidate(point, road);
			if (cand.getDistance() <= maxDist) {
				totalCands.add(cand);
			}
		}
		return totalCands;
	}

	public static PriorityQueue<Candidate> getTopKCands(
			PriorityQueue<Candidate> totalCands, int topk) {
		PriorityQueue<Candidate> candidates = totalCands;
		
		if (topk < totalCands.size()) {
			candidates = new PriorityQueue<Candidate>(Constant.DEFAULT_QUEUE_SIZE, Collections.reverseOrder());
			for (int i = 0; i < topk; i++) {
				if (totalCands.isEmpty()) {
					break;
				}
				candidates.add(totalCands.poll());
			}
		}
//
//		double probTotal = 0;
//		for (Candidate candidate: candidates) {
//			probTotal += candidate.getScore();
//		}
//
//		for (Candidate candidate: candidates) {
//			double thisScore = candidate.getScore();
//			candidate.setScore(thisScore/probTotal);
//		}

		return candidates;
	}
}
