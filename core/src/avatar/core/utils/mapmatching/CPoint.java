/*************************************************************
 * Project: Avatar Demo
 * Class: avatar.core.utils.mapmatching.CPoint
 * Author: Huajian Mao
 * Purpose: ...
 * LastChanged: 2011-12-28 14:52:07
 * Email: huajianmao@gmail.com
*************************************************************/
package avatar.core.utils.mapmatching;

import avatar.core.model.common.Point;
import avatar.core.model.geometry.Road;
import avatar.core.model.geometry.Segment;
import avatar.core.utils.common.Constant;
import avatar.core.utils.common.Measure;

public class CPoint extends Point {

	private Point point;
	private Road road;
	private int index;

	private double distToHead = -1;
	private double distToTail = -1;

	public CPoint(Point point) {
		super(point);
		this.distToHead = -1;
		this.distToTail = -1;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public Road getRoad() {
		return road;
	}

	public void setRoad(Road road) {
		this.road = road;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public double getDistance() {
		double distance = -1;
		if (point != null) {
			distance = Measure.distance(this, this.point);
		}
		return distance;
	}

	public double getDistToHead() {
		if (distToHead != -1) {
			return distToHead;
		} else if (isHead()) {
			distToHead = 0;
			return 0;
		}

		Point prevPoint = null;
		Point thisPoint = null;

		distToHead = 0;
		Point[] points = this.road.getPoints();
		for (int i = 1; i <= this.getIndex(); i++) {
			prevPoint = points[i - 1];
			thisPoint = points[i];
			double tempDist = Measure.distance(prevPoint, thisPoint);
			distToHead += tempDist;
		}
		distToHead += Measure.distance(this, points[this.getIndex()]);

		return distToHead;
	}

	public double getDistToTail() {
		if (distToTail != -1) {
			return distToTail;
		} else if (isTail()) {
			distToTail = 0;
			return distToTail;
		}

		Point prevPoint = null;
		Point thisPoint = null;

		distToTail = 0;
		Point[] points = this.road.getPoints();

		distToTail += Measure.distance(this, points[this.getIndex() + 1]);
		for (int i = this.getIndex() + 1; i < points.length - 1; i++) {
			prevPoint = points[i];
			thisPoint = points[i + 1];
			double tempDist = Measure.distance(prevPoint, thisPoint);
			distToTail += tempDist;
		}

		return distToTail;
	}

	public boolean isHead() {
		boolean head = false;
		double distance = Measure
				.distance(this, this.road.getPoints()[0]);
		if (distance < Constant.MIN_DISTANCE) {
			head = true;
		}
		return head;
	}

	public boolean isTail() {
		boolean tail = false;

		if (this.getIndex() + 1 == this.road.getPoints().length) {
			tail = true;
		}

//		Point[] points = this.road.getPoints();
//		double distance = Measure.distance(this,
//				points[points.length - 1]);
//		if (distance < Constant.MIN_DISTANCE) {
//			tail = true;
//		}
		
		return tail;
	}

	// --0--)[1---)[2---)[... ---)[n-1---
	public static CPoint getNearestPointOnRoad(Point point, Road road) {
		if (null == point || (double) -1 == point.getX()
				|| (double) -1 == point.getY() || null == road
				|| road.getPoints().length < 1) {
			Exception e = new Exception("point or road is not assigned!");
			e.printStackTrace();
		}
		double dist = -1;
		double mindist = Double.MAX_VALUE;
		Point[] points = road.getPoints();

		Point cpoint = points[0];
		int cpointIndex = 0;
		mindist = Measure.distance(point, cpoint);

		for (int i = 1; i < points.length; i++) {
			Point prevPoint = points[i - 1];
			Point thisPoint = points[i];

			Segment seg = new Segment(prevPoint, thisPoint);
			Point tempCPoint = seg.nearestTo(point);

			dist = Measure.distance(point, tempCPoint);
			if (dist < mindist) {
				mindist = dist;
				cpoint = tempCPoint;
				if (Measure.distance(cpoint, points[i]) < Constant.MIN_DISTANCE) {
					cpointIndex = i;
				} else {
					cpointIndex = i - 1;
				}
			}
		}

		CPoint cPoint = new CPoint(cpoint);
		cPoint.setPoint(point);
		cPoint.setIndex(cpointIndex);
		cPoint.setRoad(road);

		return cPoint;
	}
}
