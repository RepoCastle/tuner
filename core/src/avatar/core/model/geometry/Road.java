/*************************************************************
 * Project: Avatar Demo
 * Class: avatar.core.model.geometry.Road
 * Author: Huajian Mao
 * Purpose: ...
 * LastChanged: 2011-12-28 14:52:07
 * Email: huajianmao@gmail.com
*************************************************************/
package avatar.core.model.geometry;

import avatar.core.model.common.Edge;
import avatar.core.model.common.Point;
import avatar.core.utils.common.Constant;
import avatar.core.utils.common.Measure;

public class Road extends Edge {
	private double width;
	private Point[] points;
	private Box box;
	private int angle = -1;
	
	public static Road composeFromPoints(Point[] points) {
		Road road = new Road();
		double minlng = 360;
		double minlat = 360;
		double maxlng = -360;
		double maxlat = -360;
		for (int i=0; i<points.length; i++) {
			if (points[i].getX() > maxlng) {
				maxlng = points[i].getX();
			}
			if (points[i].getX() < minlng) {
				minlng = points[i].getX();
			}
			if (points[i].getY() > maxlat) {
				maxlat = points[i].getY();
			}
			if (points[i].getY() < minlat) {
				minlat = points[i].getY();
			}
		}
		Box box = new Box(minlng, minlat, maxlng, maxlat);
		road.setAngle(Measure.angle(points));
		road.setBox(box);
		road.setLength(Measure.distance(points));
		road.setPoints(points);
		road.setWidth(Constant.DEFAULT_ROAD_WIDTH);
		return road;
	}

	public void setPoints(Point[] points) {
		this.points = points;
		
//		double length = 0;
		this.box.setXmin(points[0].getX());
		this.box.setXmax(points[0].getX());
		this.box.setYmin(points[0].getY());
		this.box.setYmax(points[0].getY());
		
		for (int i=1; i<points.length; i++) {
//			Point prevPoint = points[i-1];
			Point thisPoint = points[i];
//			length += Measure.distance(prevPoint, thisPoint);

			this.box.setXmax(Math.max(this.box.getXmax(), thisPoint.getX()));
			this.box.setXmin(Math.min(this.box.getXmin(), thisPoint.getX()));
			this.box.setYmax(Math.max(this.box.getYmax(), thisPoint.getY()));
			this.box.setYmin(Math.min(this.box.getYmin(), thisPoint.getY()));
		}
		this.angle = Measure.angle(points);
	}

	public Point getPointAt(double length) {
		int pointCnt = this.points.length;
		Point point = new Point();
		int pointIdx = -1;
		
		double startLen = 0;
		double endLen = this.getLength();

		double pastLen = 0;
		if (Measure.isTwoDistancesSimilar(length, startLen, 1) || Measure.isTwoDistanceSimilarInPercent(length, startLen)) {
			pointIdx = 0;
			pastLen = 0;
			point = points[0];
		} else if (Measure.isTwoDistancesSimilar(length, endLen, 1) || Measure.isTwoDistanceSimilarInPercent(length, endLen)) {
			pointIdx = pointCnt - 1;
			pastLen = length;
			point = points[points.length-1];
		} else {
			for (int i=0; i<pointCnt-1; i++) {
				double thisLen = Measure.distance(points[i], points[i+1]);
				if (pastLen<=length && length<=thisLen+pastLen) {
					pointIdx = i;
					break;
				} else {
					pastLen += thisLen;
				}
			}
			
			if (pointIdx == -1) {
				point = null;
			} else {
				double leftLen = length - pastLen;
				Point pointA = this.points[pointIdx];
				Point pointB = this.points[pointIdx+1];
				double segLen = Measure.distance(pointA, pointB);
				point.x = pointA.x + (pointB.x-pointA.x) * leftLen / segLen;
				point.y = pointA.y + (pointB.y-pointA.y) * leftLen / segLen;
			}
		}
		
		if (point != null) {
			point = new Point(point);
		}
		return point;
	}

	public void setBox(Box box) {
		this.box = box;
	}
	public void setWidth(double width) {
		this.width = width;
	}
	public void setAngle(int angle) {
		this.angle = angle;
	}
	public Point[] getPoints() {
		return points;
	}
	public Box getBox() {
		return box;
	}
	public double getWidth() {
		return width;
	}
	public int getAngle() {
		return angle;
	}
	public String toString() {
		String road = "";
		road += this.getId() + ": ";
		road += this.getHead().getPoint().toString() + "\t";
		road += this.getTail().getPoint().toString();
		return road;
	}
}
